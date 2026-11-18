package com.literate.novel.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
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
import com.literate.novel.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

// Lightweight bounded LRU memory cache (12 MB max) for online novel covers
object ImageCache {
    private const val MAX_CACHE_BYTES = 12 * 1024 * 1024
    private val lru = object : LruCache<String, Bitmap>(MAX_CACHE_BYTES) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
    }

    fun get(url: String): Bitmap? = lru.get(url)
    fun put(url: String, bitmap: Bitmap) {
        lru.put(url, bitmap)
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
    showPlaceholderText: Boolean = true
) {
    var bitmap by remember(url) { mutableStateOf(ImageCache.get(url)) }

    LaunchedEffect(url) {
        if (bitmap == null && url.isNotEmpty()) {
            val loaded = withContext(Dispatchers.IO) {
                try {
                    val conn = URL(url).openConnection() as HttpURLConnection
                    conn.connectTimeout = 7000
                    conn.readTimeout = 7000
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0")
                    conn.doInput = true
                    conn.connect()
                    val bytes = conn.inputStream.use { it.readBytes() }
                    conn.disconnect()
                    decodeDownsampledBitmap(bytes)?.also { ImageCache.put(url, it) }
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
