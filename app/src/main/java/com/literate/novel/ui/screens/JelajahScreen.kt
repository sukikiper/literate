package com.literate.novel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.LiterateIcons
import com.literate.novel.ui.components.NetworkImage
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JelajahScreen(
    onNovelClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Semua") }
    var selectedStatus by remember { mutableStateOf("Semua") }
    val scope = rememberCoroutineScope()

    val genresList = listOf("Semua", "Fantasy", "Mystery", "Xianxia", "Sci-Fi", "Action", "Dark", "Comedy")
    val statusList = listOf("Semua", "Tamat", "Ongoing")

    // Live fetch from 5,000+ AniList catalog whenever search/genre/status changes
    LaunchedEffect(searchQuery, selectedGenre, selectedStatus) {
        if (searchQuery.isNotBlank()) delay(350)
        NovelRepository.loadRemoteNovels(
            search = searchQuery,
            genre = selectedGenre,
            status = selectedStatus,
            reset = true
        )
    }

    val filteredLocal = NovelRepository.novels.filter { novel ->
        val matchesQuery = searchQuery.isBlank() ||
                novel.title.contains(searchQuery, ignoreCase = true) ||
                novel.author.contains(searchQuery, ignoreCase = true)
        val matchesGenre = selectedGenre == "Semua" || novel.genres.any { it.equals(selectedGenre, ignoreCase = true) }
        val matchesStatus = selectedStatus == "Semua" || novel.status.equals(selectedStatus, ignoreCase = true)
        matchesQuery && matchesGenre && matchesStatus
    }

    val combinedNovels = remember(filteredLocal, NovelRepository.remoteNovels) {
        val seen = HashSet<String>()
        buildList {
            for (n in filteredLocal) {
                if (seen.add(n.title.lowercase())) add(n)
            }
            for (n in NovelRepository.remoteNovels) {
                if (seen.add(n.title.lowercase())) add(n)
            }
        }
    }

    val displayCountText = remember(combinedNovels.size, NovelRepository.remoteTotalCount, searchQuery, selectedGenre, selectedStatus) {
        if (searchQuery.isBlank() && selectedGenre == "Semua" && selectedStatus == "Semua") {
            val total = maxOf(combinedNovels.size, NovelRepository.remoteTotalCount + NovelRepository.novels.size)
            "$total novel ditemukan"
        } else {
            "${combinedNovels.size} novel ditemukan"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp)
    ) {
        // 1. Header Title & Subtitle
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Jelajah Novel",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cari cerita yang cocok dengan duniamu.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Search Box
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Cari judul novel atau penulis...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = CardBorder,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 3. Trending Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trending:  ",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "Lord of The Mysteries",
                    color = MintSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { searchQuery = "Lord of The Mysteries" }
                )
                Text(
                    text = "  ·  ",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "Xianxia",
                    color = MintSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { selectedGenre = "Xianxia" }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Filter Novel Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Filter Novel Header + Reset
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = LiterateIcons.Tune,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Filter Novel",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Reset",
                            color = MintSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                searchQuery = ""
                                selectedGenre = "Semua"
                                selectedStatus = "Semua"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Genre Label & Chips
                    Text(text = "Genre", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        genresList.forEach { genre ->
                            val isSelected = selectedGenre == genre
                            Surface(
                                modifier = Modifier.clickable { selectedGenre = genre },
                                color = if (isSelected) MintPrimary else ButtonDarkBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = genre,
                                    color = if (isSelected) Color.Black else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = DividerDark, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Label & Chips
                    Text(text = "Status", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        statusList.forEach { status ->
                            val isSelected = selectedStatus == status
                            Surface(
                                modifier = Modifier.clickable { selectedStatus = status },
                                color = if (isSelected) MintPrimary else ButtonDarkBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = status,
                                    color = if (isSelected) Color.Black else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 5. Semua Novel Section Title & Found Count
        item {
            SectionHeader(title = "Semua Novel")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayCountText,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Paling Populer ▾",
                    color = MintSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // 6. Novel List Items (Local NovelUpdates + Live 5,000+ AniList Catalog)
        items(combinedNovels, key = { it.id }) { novel ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onNovelClick(novel.id) },
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Novel Cover Thumbnail
                    Surface(
                        modifier = Modifier
                            .size(width = 54.dp, height = 74.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        NetworkImage(
                            url = novel.coverUrl,
                            contentDescription = novel.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = novel.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "★", color = GoldStar, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${novel.rating}",
                                color = GoldStar,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = novel.status,
                                color = CyanAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "· ${novel.chapters} ch",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = novel.genres.take(3).joinToString(" · "),
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    Icon(
                        imageVector = LiterateIcons.ChevronRight,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 7. Pagination / Footer
        item {
            Spacer(modifier = Modifier.height(14.dp))
            if (NovelRepository.isLoadingRemote) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MintPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else if (NovelRepository.hasMoreRemote) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            scope.launch {
                                NovelRepository.loadRemoteNovels(
                                    search = searchQuery,
                                    genre = selectedGenre,
                                    status = selectedStatus,
                                    reset = false
                                )
                            }
                        },
                    color = CardDark,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Muat Lebih Banyak Novel",
                            color = CyanAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else if (NovelRepository.remoteError) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            scope.launch {
                                NovelRepository.loadRemoteNovels(
                                    search = searchQuery,
                                    genre = selectedGenre,
                                    status = selectedStatus,
                                    reset = true,
                                    forceReload = true
                                )
                            }
                        },
                    color = CardDark,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Gagal memuat katalog online · Coba Lagi",
                            color = CyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                Text(
                    text = "Semua novel sudah ditampilkan.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

