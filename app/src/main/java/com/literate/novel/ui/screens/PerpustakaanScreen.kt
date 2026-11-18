package com.literate.novel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.BookshelfType
import com.literate.novel.data.Novel
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.NetworkImage
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*

@Composable
fun PerpustakaanScreen(
    onNovelClick: (String) -> Unit = {},
    onReviewClick: (Novel) -> Unit = {},
    onManageSavedClick: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Semua (5)") }

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
                text = "Perpustakaan",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Semua ceritamu, tersimpan di satu tempat.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Dual Stats Container (5 Koleksi | 1 Dirating)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: 5 Koleksi with highlighted dashed border
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0x1A2DD4BF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2DD4BF)),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "5",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Koleksi",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(DividerDark)
                    )

                    // Right: 1 Dirating
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "1",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Dirating",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Filter Pills [Semua (5)], [Disimpan (2)], [Dirating]
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("Semua (5)", "Disimpan (2)", "Dirating")
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        modifier = Modifier.clickable { selectedFilter = filter },
                        color = if (isSelected) MintPrimary else Color(0xFF1B2332),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Section: Lanjutkan Membaca
        if (selectedFilter != "Disimpan (2)") {
            item {
                SectionHeader(title = "Lanjutkan Membaca")
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Cards for currently reading (Lord of The Mysteries & A Will Eternal)
            val readingNovels = NovelRepository.novels.filter { it.bookshelf == BookshelfType.CURRENTLY_READING }
            items(readingNovels.size) { index ->
                val novel = readingNovels[index]
                val progress = novel.readingProgress
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable { onNovelClick(novel.id) },
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Top row: SEDANG DIBACA + menu
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SEDANG DIBACA",
                                color = MintSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Middle row: Thumbnail + Details
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(width = 48.dp, height = 66.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                NetworkImage(
                                    url = novel.coverUrl,
                                    contentDescription = novel.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = novel.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "★", color = GoldStar, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = "${novel.rating}", color = GoldStar, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = novel.status, color = CyanAccent, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = progress?.lastReadText ?: "Terakhir dibaca hari ini",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom progress row: Chapter X / Y and percentage
                        if (progress != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Chapter ${progress.currentChapter} / ${progress.totalChapters}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${progress.percentage}%",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            // Custom progress line
                            LinearProgressIndicator(
                                progress = { progress.percentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = MintSecondary,
                                trackColor = Color(0xFF1E283A)
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // 5. Section: Disimpan untuk Nanti
        if (selectedFilter != "Dirating") {
            item {
                SectionHeader(
                    title = "Disimpan untuk Nanti",
                    trailingText = "Kelola ›",
                    onTrailingClick = onManageSavedClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            val savedNovels = NovelRepository.novels.filter { it.bookshelf == BookshelfType.WANT_TO_READ }
            items(savedNovels.size) { index ->
                val novel = savedNovels[index]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
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
                        Surface(
                            modifier = Modifier.size(width = 48.dp, height = 66.dp),
                            shape = RoundedCornerShape(6.dp)
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
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "★", color = GoldStar, fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "${novel.rating}", color = GoldStar, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = novel.status, color = CyanAccent, fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "· ${novel.chapters} ch", color = TextMuted, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Belum dibaca · Ada di daftar bacaanmu",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // 6. Section: Selesai Dibaca
        if (selectedFilter != "Disimpan (2)") {
            item {
                SectionHeader(title = "Selesai Dibaca")
                Spacer(modifier = Modifier.height(10.dp))

                val finishedNovel = NovelRepository.novels.find { it.bookshelf == BookshelfType.READ }
                    ?: NovelRepository.novels.first()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNovelClick(finishedNovel.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(width = 48.dp, height = 66.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                NetworkImage(
                                    url = finishedNovel.coverUrl,
                                    contentDescription = finishedNovel.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = finishedNovel.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "★", color = GoldStar, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = "${finishedNovel.rating}", color = GoldStar, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = finishedNovel.status, color = CyanAccent, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "· ${finishedNovel.chapters} ch", color = TextMuted, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "1200 / 1200 chapters · 100% selesai",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = DividerDark, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom action: Bacaan dituntaskan + Beri Ulasan ›
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Bacaan dituntaskan",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "Beri Ulasan ›",
                                color = MintSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onReviewClick(finishedNovel) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

