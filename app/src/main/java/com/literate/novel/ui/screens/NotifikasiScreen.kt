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
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*

@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit = {}
) {
    val initial = NovelRepository.notificationSettings
    var appNotification by remember { mutableStateOf(initial.appNotification) }
    var emailNotification by remember { mutableStateOf(initial.emailNotification) }
    var newChapter by remember { mutableStateOf(initial.newChapter) }
    var reviewReply by remember { mutableStateOf(initial.reviewReply) }
    var novelRecommendation by remember { mutableStateOf(initial.novelRecommendation) }
    var readingReminder by remember { mutableStateOf(initial.readingReminder) }
    var quietTimeEnabled by remember { mutableStateOf(initial.quietTimeEnabled) }
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
                text = "Notifikasi",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pilih kabar yang ingin kamu terima.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Section: Saluran Notifikasi
        item {
            SectionHeader(title = "Saluran Notifikasi")
            Spacer(modifier = Modifier.height(10.dp))

            NotificationToggleCard(
                title = "Notifikasi Aplikasi",
                subtitle = "Terima pemberitahuan langsung di perangkat.",
                checked = appNotification,
                onCheckedChange = { appNotification = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NotificationToggleCard(
                title = "Notifikasi Email",
                subtitle = "Kirim ringkasan aktivitas ke alamat emailmu.",
                checked = emailNotification,
                onCheckedChange = { emailNotification = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Section: Novel & Aktivitas
        item {
            SectionHeader(title = "Novel & Aktivitas")
            Spacer(modifier = Modifier.height(10.dp))

            NotificationToggleCard(
                title = "Chapter Baru",
                subtitle = "Pembaruan novel yang ada di perpustakaanmu.",
                checked = newChapter,
                onCheckedChange = { newChapter = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NotificationToggleCard(
                title = "Balasan Ulasan",
                subtitle = "Saat pembaca lain membalas ulasanmu.",
                checked = reviewReply,
                onCheckedChange = { reviewReply = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NotificationToggleCard(
                title = "Rekomendasi Novel",
                subtitle = "Cerita pilihan sesuai genre favoritmu.",
                checked = novelRecommendation,
                onCheckedChange = { novelRecommendation = it }
            )
            Spacer(modifier = Modifier.height(6.dp))
            NotificationToggleCard(
                title = "Pengingat Membaca",
                subtitle = "Luangkan waktu untuk melanjutkan cerita.",
                checked = readingReminder,
                onCheckedChange = { readingReminder = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Section: Waktu Tenang
        item {
            SectionHeader(title = "Waktu Tenang")
            Spacer(modifier = Modifier.height(10.dp))

            NotificationToggleCard(
                title = "Jeda Notifikasi",
                subtitle = "Nonaktifkan suara dan notifikasi sementara.",
                checked = quietTimeEnabled,
                onCheckedChange = { quietTimeEnabled = it }
            )
            Spacer(modifier = Modifier.height(6.dp))

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Setiap hari", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "22.00 – 07.00",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Ubah ›",
                        color = MintSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Simpan Preferensi Button
            Button(
                onClick = {
                    NovelRepository.updateNotificationSettings(
                        initial.copy(
                            appNotification = appNotification,
                            emailNotification = emailNotification,
                            newChapter = newChapter,
                            reviewReply = reviewReply,
                            novelRecommendation = novelRecommendation,
                            readingReminder = readingReminder,
                            quietTimeEnabled = quietTimeEnabled
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
                    text = if (isSaved) "Preferensi Disimpan!" else "Simpan Preferensi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun NotificationToggleCard(
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

