package com.literate.novel.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class SakuraPageResult(
    val novels: List<Novel>,
    val hasNextPage: Boolean,
    val totalCount: Int
)

object SakuraNovelApi {
    private const val SERI_ENDPOINT = "https://sakuranovel.id/wp-json/wp/v2/seri"
    private const val POSTS_ENDPOINT = "https://sakuranovel.id/wp-json/wp/v2/posts"

    suspend fun fetchNovels(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null
    ): SakuraPageResult = withContext(Dispatchers.IO) {
        val sb = StringBuilder("$SERI_ENDPOINT?per_page=$perPage&page=$page&orderby=count&order=desc")
        if (!search.isNullOrBlank()) {
            val encoded = URLEncoder.encode(search.trim(), "UTF-8")
            sb.append("&search=").append(encoded)
        }
        val requestUrl = sb.toString()
        val cacheKey = "sakura_seri_$requestUrl"

        // 1. Instant hit from persistent disk cache (24h TTL)
        LiterateCache.getCachedApiResponse(cacheKey, allowExpired = false)?.let { cached ->
            parseCachedSeriPayload(cached, page)?.let { return@withContext it }
        }

        val conn = (URL(requestUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) LiterateApp/1.0")
        }

        try {
            val code = conn.responseCode
            if (code !in 200..299) {
                // Offline/error fallback to expired disk cache if available
                LiterateCache.getCachedApiResponse(cacheKey, allowExpired = true)?.let { stale ->
                    parseCachedSeriPayload(stale, page)?.let { return@withContext it }
                }
                return@withContext SakuraPageResult(emptyList(), false, 0)
            }

            val total = conn.getHeaderField("X-WP-Total")?.toIntOrNull() ?: 0
            val totalPages = conn.getHeaderField("X-WP-TotalPages")?.toIntOrNull() ?: 1
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            if (text.isBlank()) {
                return@withContext SakuraPageResult(emptyList(), false, total)
            }

            // Save wrapped response (with total & totalPages) to disk cache
            val wrapper = JSONObject().apply {
                put("total", total)
                put("totalPages", totalPages)
                put("items", JSONArray(text))
            }
            LiterateCache.putCachedApiResponse(cacheKey, wrapper.toString())

            parseSeriArray(wrapper.getJSONArray("items"), page, totalPages, total)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            // Offline fallback to expired cache
            LiterateCache.getCachedApiResponse(cacheKey, allowExpired = true)?.let { stale ->
                parseCachedSeriPayload(stale, page)?.let { return@withContext it }
            }
            SakuraPageResult(emptyList(), false, 0)
        } finally {
            conn.disconnect()
        }
    }

    private fun parseCachedSeriPayload(cachedJson: String, page: Int): SakuraPageResult? {
        return try {
            val obj = JSONObject(cachedJson)
            val total = obj.optInt("total", 0)
            val totalPages = obj.optInt("totalPages", 1)
            val arr = obj.optJSONArray("items") ?: return null
            parseSeriArray(arr, page, totalPages, total)
        } catch (_: Exception) {
            null
        }
    }

    private fun parseSeriArray(arr: JSONArray, page: Int, totalPages: Int, total: Int): SakuraPageResult {
        val list = ArrayList<Novel>(arr.length())
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val wpId = obj.optInt("id", 0)
            if (wpId == 0) continue

            val rawName = obj.optString("name", "").trim()
            val title = decodeHtmlEntities(rawName)
            if (title.isBlank()) continue

            val chapterCount = obj.optInt("count", 0).coerceAtLeast(1)
            val rawDesc = decodeHtmlEntities(obj.optString("description", ""))
            val synopsis = rawDesc.ifBlank {
                "Novel terjemahan Bahasa Indonesia dari katalog SakuraNovel dengan total $chapterCount chapter. Tambahkan ke perpustakaan dan bagikan ulasanmu bersama komunitas LITERATE."
            }

            val inferredGenres = inferGenresFromTitle(title)
            val status = if (chapterCount >= 400) "Tamat" else "Ongoing"
            val rating = (4.3 + ((wpId % 6) * 0.1)).coerceAtMost(4.9)
            val votes = (chapterCount * 2 + (wpId % 350)).coerceAtLeast(85)

            list.add(
                Novel(
                    id = "sn_$wpId",
                    title = title,
                    coverUrl = "",
                    rating = (kotlin.math.round(rating * 10.0) / 10.0),
                    totalVotes = votes,
                    status = status,
                    chapters = chapterCount,
                    genres = inferredGenres,
                    author = "SakuraNovel",
                    originalLanguage = "Bahasa Indonesia (TL)",
                    releaseYear = 2024,
                    synopsis = synopsis,
                    chaptersList = listOf(
                        ChapterItem(1, "Chapter 1: Awal Cerita", "1.5k reads", "8 min read"),
                        ChapterItem(2, "Chapter 2: Pertemuan Baru", "1.2k reads", "9 min read"),
                        ChapterItem(3, "Chapter 3: Rahasia Terungkap", "1.0k reads", "8 min read")
                    ),
                    reviews = listOf(
                        ReviewItem(
                            id = "rev_sn_${wpId}_1",
                            userName = "PembacaSakura",
                            avatarColor = 0xFF6366F1,
                            stars = 4.5f,
                            content = "Terjemahan enak dibaca dan alurnya bikin betah maraton!"
                        )
                    )
                )
            )
        }
        return SakuraPageResult(
            novels = list,
            hasNextPage = page < totalPages,
            totalCount = total
        )
    }

    suspend fun fetchChaptersForSeri(wpSeriId: Int): List<ChapterItem> = withContext(Dispatchers.IO) {
        val url = "$POSTS_ENDPOINT?seri=$wpSeriId&per_page=5"
        val cacheKey = "sakura_chapters_$wpSeriId"

        LiterateCache.getCachedApiResponse(cacheKey, allowExpired = false)?.let { cached ->
            val parsed = parseChaptersJson(cached)
            if (parsed.isNotEmpty()) return@withContext parsed
        }

        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5000
            readTimeout = 5000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) LiterateApp/1.0")
        }
        try {
            if (conn.responseCode !in 200..299) {
                LiterateCache.getCachedApiResponse(cacheKey, allowExpired = true)?.let { stale ->
                    return@withContext parseChaptersJson(stale)
                }
                return@withContext emptyList()
            }
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            LiterateCache.putCachedApiResponse(cacheKey, text)
            parseChaptersJson(text)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            LiterateCache.getCachedApiResponse(cacheKey, allowExpired = true)?.let { stale ->
                return@withContext parseChaptersJson(stale)
            }
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    private fun parseChaptersJson(text: String): List<ChapterItem> {
        return try {
            val arr = JSONArray(text)
            val result = ArrayList<ChapterItem>(arr.length())
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val titleObj = obj.optJSONObject("title")
                val chTitle = decodeHtmlEntities(titleObj?.optString("rendered", "") ?: "")
                    .replace("\n", " ")
                    .replace(Regex("\\s+"), " ")
                    .trim()
                if (chTitle.isNotBlank()) {
                    result.add(
                        ChapterItem(
                            number = i + 1,
                            title = chTitle,
                            reads = "${1200 + (arr.length() - i) * 180} reads",
                            readTime = "${7 + (i % 4)} min read"
                        )
                    )
                }
            }
            result
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun inferGenresFromTitle(title: String): List<String> {
        val lower = title.lowercase()
        val genres = mutableListOf<String>()
        if (lower.contains("hero") || lower.contains("sword") || lower.contains("knight") || lower.contains("hunter") || lower.contains("academy") || lower.contains("battle")) {
            genres.add("Action")
        }
        if (lower.contains("villain") || lower.contains("kill") || lower.contains("demon") || lower.contains("dark") || lower.contains("death") || lower.contains("yandere")) {
            genres.add("Dark")
        }
        if (lower.contains("mystery") || lower.contains("secret") || lower.contains("shadow") || lower.contains("regression") || lower.contains("time")) {
            genres.add("Mystery")
        }
        if (lower.contains("cultivat") || lower.contains("immortal") || lower.contains("martial") || lower.contains("dao") || lower.contains("murim")) {
            genres.add("Xianxia")
        }
        if (lower.contains("mech") || lower.contains("cyber") || lower.contains("space") || lower.contains("system") || lower.contains("game")) {
            genres.add("Sci-Fi")
        }
        if (lower.contains("flirt") || lower.contains("love") || lower.contains("heroine") || lower.contains("senior") || lower.contains("wife") || lower.contains("comedy")) {
            genres.add("Comedy")
        }
        if (genres.isEmpty() || !genres.contains("Fantasy")) {
            genres.add(0, "Fantasy")
        }
        return genres.take(3)
    }

    private fun decodeHtmlEntities(raw: String): String {
        if (raw.isBlank() || raw == "null") return ""
        return raw
            .replace(Regex("<[^>]*>"), "")
            .replace("&#8217;", "’")
            .replace("&#8216;", "‘")
            .replace("&#8220;", "“")
            .replace("&#8221;", "”")
            .replace("&#8211;", "–")
            .replace("&#8212;", "—")
            .replace("&#038;", "&")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .trim()
    }
}

