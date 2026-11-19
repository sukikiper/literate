package com.literate.novel.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.LiterateCache
import com.literate.novel.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// 2-Layer RAM + Disk Image Cache & Persistent Cover URL Resolver
object ImageCache {
    private val resolveSemaphore = Semaphore(4)

    fun getRam(key: String): Bitmap? = LiterateCache.getBitmapFromRam(key)
    fun getDisk(key: String): Bitmap? = LiterateCache.getBitmapFromDisk(key)
    fun put(key: String, bitmap: Bitmap) = LiterateCache.putBitmap(key, bitmap)

    suspend fun resolveCoverUrl(title: String): String {
        val cleanKey = title.trim().lowercase()
        if (cleanKey.isBlank()) return ""
        LiterateCache.getCachedCoverUrl(cleanKey)?.let { return it }

        return resolveSemaphore.withPermit {
            LiterateCache.getCachedCoverUrl(cleanKey)?.let { return@withPermit it }
            val found = fetchAniListCover(title)
            LiterateCache.putCachedCoverUrl(cleanKey, found)
            found
        }
    }

    private fun fetchAniListCover(title: String): String {
        val conn = (URL("https://graphql.anilist.co").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 4500
            readTimeout = 4500
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "LiterateAndroid/1.0")
        }
        return try {
            val payload = JSONObject().apply {
                put("query", "query (\$s: String) { Media(search: \$s, type: MANGA) { coverImage { large } } }")
                put("variables", JSONObject().put("s", title))
            }.toString()
            conn.outputStream.use { it.write(payload.toByteArray(Charsets.UTF_8)) }
            if (conn.responseCode in 200..299) {
                val text = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(text)
                    .optJSONObject("data")
                    ?.optJSONObject("Media")
                    ?.optJSONObject("coverImage")
                    ?.optString("large", "")
                    ?.takeIf { it.startsWith("http") } ?: ""
            } else ""
        } catch (_: Exception) {
            ""
        } finally {
            conn.disconnect()
        }
    }
}

private fun decodeDownsampledBitmap(bytes: ByteArray, reqMaxPx: Int = 480): Bitmap? {
    val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, boundsOpts)
    var sampleSize = 1
    val maxDim = maxOf(boundsOpts.outWidth, boundsOpts.outHeight)
    while (maxDim / sampleSize > reqMaxPx) {
        sampleSize *= 2
    }
    val decodeOpts = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
        inPreferredConfig = Bitmap.Config.RGB_565 // 50% less RAM per image
    }
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)
}

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    showPlaceholderText: Boolean = true,
    fallbackTitle: String? = contentDescription
) {
    val cacheKey = remember(url, fallbackTitle) {
        if (url.isNotBlank()) url else "title:${fallbackTitle?.trim()?.lowercase().orEmpty()}"
    }
    var bitmap by remember(cacheKey) {
        mutableStateOf(
            if (url.startsWith("asset://")) {
                LiterateCache.getBitmapFromAssets(url.removePrefix("asset://"))
            } else {
                ImageCache.getRam(cacheKey)
            }
        )
    }

    LaunchedEffect(cacheKey) {
        if (bitmap == null) {
            val loaded = withContext(Dispatchers.IO) {
                try {
                    if (url.startsWith("asset://")) {
                        return@withContext LiterateCache.getBitmapFromAssets(url.removePrefix("asset://"))
                    }
                    // 1. Check persistent disk cache first (< 5ms, works offline!)
                    ImageCache.getDisk(cacheKey)?.let { return@withContext it }

                    val targetUrl = if (url.isNotBlank()) {
                        url
                    } else if (!fallbackTitle.isNullOrBlank()) {
                        ImageCache.resolveCoverUrl(fallbackTitle)
                    } else ""

                    if (targetUrl.isBlank()) return@withContext null

                    ImageCache.getDisk(targetUrl)?.let {
                        ImageCache.put(cacheKey, it)
                        return@withContext it
                    }

                    val conn = URL(targetUrl).openConnection() as HttpURLConnection
                    conn.connectTimeout = 6000
                    conn.readTimeout = 6000
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0")
                    conn.doInput = true
                    conn.connect()
                    val bytes = conn.inputStream.use { it.readBytes() }
                    conn.disconnect()
                    decodeDownsampledBitmap(bytes)?.also {
                        ImageCache.put(cacheKey, it)
                        if (targetUrl != cacheKey) ImageCache.put(targetUrl, it)
                    }
                } catch (_: Exception) {
                    null
                }
            }
            if (loaded != null) {
                bitmap = loaded
            }
        }
    }

    val currentBmp = bitmap
    if (currentBmp != null) {
        Image(
            bitmap = currentBmp.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        // Styled placeholder matching mockup: #1F1E2E with [ Cover ]
        Box(
            modifier = modifier.background(InputBg),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = LiterateIcons.ImagePlaceholder,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(22.dp)
                )
                if (showPlaceholderText) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "[ Cover ]",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun LiterateTopBar(
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgDark)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = TextPrimary,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // LITERATE Badge Logo (#D4FFF6 icy mint background, black bold text)
        Surface(
            color = MintPrimary,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            Text(
                text = "LITERATE",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        TextButton(
            onClick = onSignInClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Sign in",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Button(
            onClick = onSignUpClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MintPrimary,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp)
        ) {
            Text(
                text = "Sign up",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

enum class NavigationTab {
    BERANDA, CARI, PERPUSTAKAAN, PROFIL
}

@Composable
fun LiterateBottomBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    Surface(
        color = CardDark,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = "Beranda",
                isSelected = currentTab == NavigationTab.BERANDA,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                onClick = { onTabSelected(NavigationTab.BERANDA) }
            )
            BottomNavItem(
                label = "Cari",
                isSelected = currentTab == NavigationTab.CARI,
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                onClick = { onTabSelected(NavigationTab.CARI) }
            )
            BottomNavItem(
                label = "Perpustakaan",
                isSelected = currentTab == NavigationTab.PERPUSTAKAAN,
                selectedIcon = LiterateIcons.Bookmark,
                unselectedIcon = LiterateIcons.BookmarkBorder,
                onClick = { onTabSelected(NavigationTab.PERPUSTAKAAN) }
            )
            BottomNavItem(
                label = "Profil",
                isSelected = currentTab == NavigationTab.PROFIL,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                onClick = { onTabSelected(NavigationTab.PROFIL) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    isSelected: Boolean,
    selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = if (isSelected) MintPrimary else TextMuted,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) MintPrimary else TextMuted,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    onTrailingClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            if (trailingText != null && onTrailingClick != null) {
                Text(
                    text = trailingText,
                    color = CyanAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onTrailingClick)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(3.dp)
                .background(MintPrimary, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun StarDisplay(
    rating: Double,
    maxStars: Int = 5,
    starSize: Dp = 14.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val filled = rating >= i
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) GoldStar else ButtonDarkBg,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
