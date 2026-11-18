package com.literate.novel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.NetworkImage
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*

@Composable
fun ProfilScreen(
    onNavigateToAccountInfo: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    onNovelClick: (String) -> Unit = {},
    onEditReviewClick: () -> Unit = {}
) {
    val profile = NovelRepository.userProfile
    val myReview = NovelRepository.myReview
    val lotmNovel = NovelRepository.getNovelById("lotm") ?: NovelRepository.novels.first()

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
                text = "Profil",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ruang untuk semua cerita tentangmu.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. User Info Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar [ Foto ]
                        Surface(
                            modifier = Modifier.size(54.dp),
                            shape = CircleShape,
                            color = Color(0xFF192030),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "[ Foto ]",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = profile.name,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = profile.username,
                                color = MintSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = profile.bio,
                        color = Color(0xFFE5E7EB),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Edit Profil Button
                    Button(
                        onClick = onNavigateToAccountInfo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(21.dp)
                    ) {
                        Text(
                            text = "Edit Profil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. Stats Tri-Card (5 Koleksi | 2 Dibaca | 1 Selesai)
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
                    StatColumn(number = "${profile.totalCollections}", label = "Koleksi")
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(DividerDark))
                    StatColumn(number = "${profile.totalReading}", label = "Dibaca")
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(DividerDark))
                    StatColumn(number = "${profile.totalFinished}", label = "Selesai")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Section: Genre Favorit
        item {
            SectionHeader(title = "Genre Favorit")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profile.favoriteGenres.forEach { genre ->
                    Surface(
                        color = Color(0xFF1B2332),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = genre,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 5. Section: Rating & Ulasan Saya
        item {
            SectionHeader(
                title = "Rating & Ulasan Saya",
                trailingText = "Lihat semua ›",
                onTrailingClick = {}
            )
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Cover Thumbnail
                    Surface(
                        modifier = Modifier
                            .size(width = 52.dp, height = 72.dp)
                            .clickable { onNovelClick("lotm") },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        NetworkImage(
                            url = lotmNovel.coverUrl,
                            contentDescription = lotmNovel.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lotmNovel.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (i in 1..5) {
                                Text(text = "★", color = GoldStar, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "5.0",
                                color = GoldStar,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = myReview.content,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Ubah ulasan ›",
                            color = MintSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(onClick = onEditReviewClick)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 6. Section: Pengaturan Akun
        item {
            SectionHeader(title = "Pengaturan Akun")
            Spacer(modifier = Modifier.height(10.dp))

            AccountMenuItem(
                title = "Informasi Akun",
                subtitle = "Nama, email, dan kata sandi",
                onClick = onNavigateToAccountInfo
            )
            Spacer(modifier = Modifier.height(6.dp))
            AccountMenuItem(
                title = "Notifikasi",
                subtitle = "Atur pembaruan novel dan aktivitas",
                onClick = onNavigateToNotification
            )
            Spacer(modifier = Modifier.height(6.dp))
            AccountMenuItem(
                title = "Privasi",
                subtitle = "Kelola visibilitas profil dan ulasan",
                onClick = onNavigateToPrivacy
            )
            Spacer(modifier = Modifier.height(6.dp))
            AccountMenuItem(
                title = "Bantuan & Tentang",
                subtitle = "Pusat bantuan dan informasi LITERATE",
                onClick = onNavigateToHelp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Keluar Akun Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {},
                color = Color(0xFF222838),
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
                        text = "Keluar Akun",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer
            Text(
                text = "LITERATE · Temukan cerita berikutnya.",
                color = TextMuted,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun StatColumn(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun AccountMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = CardDark,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

