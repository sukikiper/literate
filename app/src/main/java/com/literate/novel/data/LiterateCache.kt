package com.literate.novel.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

/**
 * 2-Layer Cache System (RAM LRU + Persistent Disk Cache up to 150 MB)
 * - Images: RAM LruCache (24 MB) + Disk JPEG cache (up to 150 MB, auto-evicts oldest)
 * - API JSON (SakuraNovel catalog, search, chapters): Disk cache with 24h TTL + offline fallback
 * - Cover URL map (SakuraNovel title -> HD cover URL): Persisted JSON map on disk
 */
object LiterateCache {
    private const val MAX_RAM_BYTES = 24 * 1024 * 1024          // 24 MB RAM (smooth & lightweight)
    private const val MAX_DISK_IMAGE_BYTES = 150L * 1024 * 1024 // 150 MB Disk image cache
    private const val API_TTL_MS = 24L * 60 * 60 * 1000         // 24 hours

    private var rootDir: File? = null
    private var imagesDir: File? = null
    private var apiDir: File? = null
    private var coverMapFile: File? = null

    private val ramImages = object : LruCache<String, Bitmap>(MAX_RAM_BYTES) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
    }

    private val coverUrlMap = ConcurrentHashMap<String, String>()

    fun init(context: Context) {
        if (rootDir != null) return
        val base = File(context.applicationContext.cacheDir, "literate_cache")
        val img = File(base, "images")
        val api = File(base, "api")
        img.mkdirs()
        api.mkdirs()
        rootDir = base
        imagesDir = img
        apiDir = api
        coverMapFile = File(base, "cover_urls.json")
        loadCoverUrlMapFromDisk()
    }

    private fun md5(key: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(key.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // --- 1. Image RAM + Disk Cache ---

    fun getBitmapFromRam(key: String): Bitmap? = ramImages.get(key)

    fun getBitmapFromDisk(key: String): Bitmap? {
        ramImages.get(key)?.let { return it }
        val dir = imagesDir ?: return null
        val file = File(dir, "${md5(key)}.jpg")
        if (!file.exists()) return null
        return try {
            file.setLastModified(System.currentTimeMillis())
            val opts = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.RGB_565
            }
            BitmapFactory.decodeFile(file.absolutePath, opts)?.also {
                ramImages.put(key, it)
            }
        } catch (_: Exception) {
            null
        }
    }

    fun putBitmap(key: String, bitmap: Bitmap) {
        ramImages.put(key, bitmap)
        val dir = imagesDir ?: return
        try {
            val file = File(dir, "${md5(key)}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            trimImageDiskCacheIfNeeded(dir)
        } catch (_: Exception) {
            // Ignore disk write errors
        }
    }

    private fun trimImageDiskCacheIfNeeded(dir: File) {
        val files = dir.listFiles() ?: return
        var totalSize = files.sumOf { it.length() }
        if (totalSize <= MAX_DISK_IMAGE_BYTES) return
        val sorted = files.sortedBy { it.lastModified() }
        for (f in sorted) {
            if (totalSize <= MAX_DISK_IMAGE_BYTES * 80 / 100) break
            val len = f.length()
            if (f.delete()) {
                totalSize -= len
            }
        }
    }

    // --- 2. Cover URL Map Persistent Cache ---

    fun getCachedCoverUrl(titleKey: String): String? = coverUrlMap[titleKey]

    fun putCachedCoverUrl(titleKey: String, url: String) {
        coverUrlMap[titleKey] = url
        saveCoverUrlMapToDisk()
    }

    private fun loadCoverUrlMapFromDisk() {
        val file = coverMapFile ?: return
        if (!file.exists()) return
        try {
            val json = JSONObject(file.readText(Charsets.UTF_8))
            val keys = json.keys()
            while (keys.hasNext()) {
                val k = keys.next()
                coverUrlMap[k] = json.optString(k, "")
            }
        } catch (_: Exception) {
        }
    }

    @Synchronized
    private fun saveCoverUrlMapToDisk() {
        val file = coverMapFile ?: return
        try {
            val json = JSONObject()
            for ((k, v) in coverUrlMap) {
                json.put(k, v)
            }
            file.writeText(json.toString(), Charsets.UTF_8)
        } catch (_: Exception) {
        }
    }

    // --- 3. API Response Disk Cache (with TTL + Offline Fallback) ---

    fun getCachedApiResponse(cacheKey: String, allowExpired: Boolean = false): String? {
        val dir = apiDir ?: return null
        val file = File(dir, "${md5(cacheKey)}.json")
        if (!file.exists()) return null
        val age = System.currentTimeMillis() - file.lastModified()
        if (!allowExpired && age > API_TTL_MS) return null
        return try {
            file.readText(Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    fun putCachedApiResponse(cacheKey: String, jsonText: String) {
        val dir = apiDir ?: return
        try {
            val file = File(dir, "${md5(cacheKey)}.json")
            file.writeText(jsonText, Charsets.UTF_8)
        } catch (_: Exception) {
        }
    }

    // --- 4. Cache Stats & Clear ---

    fun getTotalDiskCacheBytes(): Long {
        val dir = rootDir ?: return 0L
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    fun getFormattedCacheSize(): String {
        val bytes = getTotalDiskCacheBytes()
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        }
    }

    fun clearAll() {
        ramImages.evictAll()
        coverUrlMap.clear()
        try {
            imagesDir?.listFiles()?.forEach { it.delete() }
            apiDir?.listFiles()?.forEach { it.delete() }
            coverMapFile?.delete()
        } catch (_: Exception) {
        }
    }
}

