package com.literate.novel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.LiterateIcons
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*

@Composable
fun PrivasiScreen(
    onBackClick: () -> Unit = {}
) {
    val initial = NovelRepository.privacySettings
    var visibility by remember { mutableStateOf(initial.profileVisibility) }
    var showLibrary by remember { mutableStateOf(initial.showLibrary) }
    var showProgress by remember { mutableStateOf(initial.showReadingProgress) }
    var showRating by remember { mutableStateOf(initial.showRatingAndReview) }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp)
    ) {
        // 1. Back link
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "‹ Profil / Pengaturan",
                color = MintSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(onClick = onBackClick)
                    .padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // 2. Title & Subtitle
        item {
            Text(
                text = "Privasi",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tentukan apa yang dapat dilihat pembaca lain.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Section: Visibilitas Profil
        item {
            SectionHeader(title = "Visibilitas Profil")
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Siapa yang bisa melihat profilmu?",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Publik Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { visibility = "Publik" },
                            color = if (visibility == "Publik") MintPrimary else InputBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "•  Publik",
                                    color = if (visibility == "Publik") Color.Black else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Privat Option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { visibility = "Privat" },
                            color = if (visibility == "Privat") MintPrimary else InputBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "○  Privat",
                                    color = if (visibility == "Privat") Color.Black else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (visibility == "Publik") "Publik: profil bisa dilihat semua pembaca." else "Privat: profil hanya bisa dilihat oleh kamu.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Section: Aktivitas & Koleksi
        item {
            SectionHeader(title = "Aktivitas & Koleksi")
            Spacer(modifier = Modifier.height(10.dp))

            PrivacyToggleCard(
                title = "Tampilkan Perpustakaan",
                subtitle = "Izinkan pembaca lain melihat koleksimu.",
                checked = showLibrary,
                onCheckedChange = { showLibrary = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            PrivacyToggleCard(
                title = "Tampilkan Progres Bacaan",
                subtitle = "Bagikan novel dan progres yang sedang dibaca.",
                checked = showProgress,
                onCheckedChange = { showProgress = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            PrivacyToggleCard(
                title = "Tampilkan Rating & Ulasan",
                subtitle = "Ulasanmu dapat muncul di profil publik.",
                checked = showRating,
                onCheckedChange = { showRating = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Section: Data & Keamanan
        item {
            SectionHeader(title = "Data & Keamanan")
            Spacer(modifier = Modifier.height(10.dp))

            PrivacyLinkItem(
                title = "Unduh Data Saya",
                subtitle = "Dapatkan salinan profil, koleksi, dan ulasan."
            )
            Spacer(modifier = Modifier.height(6.dp))
            PrivacyLinkItem(
                title = "Akun Diblokir",
                subtitle = "Kelola pembaca yang kamu blokir."
            )
            Spacer(modifier = Modifier.height(6.dp))
            PrivacyLinkItem(
                title = "Kebijakan Privasi",
                subtitle = "Pelajari bagaimana data kamu digunakan."
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Simpan Pengaturan Button
            Button(
                onClick = {
                    NovelRepository.updatePrivacySettings(
                        initial.copy(
                            profileVisibility = visibility,
                            showLibrary = showLibrary,
                            showReadingProgress = showProgress,
                            showRatingAndReview = showRating
                        )
                    )
                    isSaved = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintPrimary,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(23.dp)
            ) {
                Text(
                    text = if (isSaved) "Pengaturan Disimpan!" else "Simpan Pengaturan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Pengaturan privasi tidak menghapus koleksi bacaanmu.",
                color = TextMuted,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun PrivacyToggleCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BgDark,
                    checkedTrackColor = MintPrimary,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = ButtonDarkBg
                )
            )
        }
    }
}

@Composable
private fun PrivacyLinkItem(title: String, subtitle: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
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
                imageVector = LiterateIcons.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

