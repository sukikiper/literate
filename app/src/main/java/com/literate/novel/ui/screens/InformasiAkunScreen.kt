package com.literate.novel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
fun InformasiAkunScreen(
    onBackClick: () -> Unit = {},
    onShowMessage: (String) -> Unit = {}
) {
    val initialProfile = NovelRepository.userProfile
    var name by remember { mutableStateOf(initialProfile.name) }
    var username by remember { mutableStateOf(initialProfile.username) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var bio by remember { mutableStateOf(initialProfile.bio) }
    var isSaved by remember { mutableStateOf(false) }

    var showAvatarDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDevicesDialog by remember { mutableStateOf(false) }

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
                text = "Informasi Akun",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kelola identitas dan keamanan akunmu.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Avatar Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable { showAvatarDialog = true },
                        shape = CircleShape,
                        color = Color(NovelRepository.avatarColor),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = NovelRepository.avatarText,
                                color = if (NovelRepository.avatarText == "[ Foto ]") TextSecondary else Color.White,
                                fontSize = if (NovelRepository.avatarText == "[ Foto ]") 11.sp else 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = name,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ganti foto profil",
                            color = MintSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { showAvatarDialog = true }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Section: Data Pribadi
        item {
            SectionHeader(title = "Data Pribadi")
            Spacer(modifier = Modifier.height(12.dp))

            // Nama Lengkap
            Text(text = "Nama lengkap", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = MintSecondary,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Nama Pengguna
            Text(text = "Nama pengguna", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = MintSecondary,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Alamat Email
            Text(text = "Alamat email", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = MintSecondary,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Email hanya terlihat olehmu.",
                color = TextMuted,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Bio
            Text(text = "Bio", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = MintSecondary,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 5. Section: Keamanan
        item {
            SectionHeader(title = "Keamanan")
            Spacer(modifier = Modifier.height(12.dp))

            // Ubah Kata Sandi
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPasswordDialog = true },
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
                        Text(
                            text = "Ubah Kata Sandi",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Perbarui kata sandi untuk melindungi akun.",
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

            Spacer(modifier = Modifier.height(8.dp))

            // Perangkat Terhubung
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDevicesDialog = true },
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
                        Text(
                            text = "Perangkat Terhubung",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "1 perangkat aktif · perangkat ini",
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

            Spacer(modifier = Modifier.height(20.dp))

            // Simpan Perubahan Button
            Button(
                onClick = {
                    NovelRepository.updateUserProfile(name, username, email, bio)
                    isSaved = true
                    onShowMessage("Perubahan informasi akun tersimpan!")
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
                    text = if (isSaved) "Tersimpan!" else "Simpan Perubahan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Perubahan berlaku untuk profil LITERATE kamu.",
                color = TextMuted,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    if (showAvatarDialog) {
        val initials = name.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.take(1).uppercase() }.ifBlank { "PN" }
        val options = listOf(
            Triple(initials, 0xFF0EA5E9L, "Biru Muda"),
            Triple(initials, 0xFF8B5CF6L, "Ungu"),
            Triple(initials, 0xFF10B981L, "Emerald"),
            Triple(initials, 0xFFD97706L, "Emas"),
            Triple("[ Foto ]", 0xFF1F1E2EL, "Default")
        )
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            containerColor = CardDark,
            title = { Text("Pilih Foto Profil", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { (label, colorHex, title) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    NovelRepository.updateAvatar(label, colorHex)
                                    showAvatarDialog = false
                                    onShowMessage("Foto profil diperbarui ($title)")
                                },
                            color = InputBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(34.dp),
                                    shape = CircleShape,
                                    color = Color(colorHex)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(label, color = Color.White, fontSize = if (label == "[ Foto ]") 8.sp else 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(title, color = TextPrimary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) { Text("Tutup", color = CyanAccent) }
            }
        )
    }

    if (showPasswordDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            containerColor = CardDark,
            title = { Text("Ubah Kata Sandi", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        placeholder = { Text("Kata sandi saat ini", color = TextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanAccent, unfocusedBorderColor = CardBorder
                        )
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        placeholder = { Text("Kata sandi baru (min. 6 karakter)", color = TextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanAccent, unfocusedBorderColor = CardBorder
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPass.length >= 6) {
                        showPasswordDialog = false
                        onShowMessage("Kata sandi berhasil diperbarui!")
                    } else {
                        onShowMessage("Kata sandi baru minimal 6 karakter.")
                    }
                }) { Text("Simpan", color = MintPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Batal", color = TextSecondary) }
            }
        )
    }

    if (showDevicesDialog) {
        AlertDialog(
            onDismissRequest = { showDevicesDialog = false },
            containerColor = CardDark,
            title = { Text("Perangkat Terhubung", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("• Perangkat Android ini (Aktif sekarang)", color = TextPrimary, fontSize = 13.sp)
                    Text("Akun kamu aman dan hanya masuk di 1 perangkat.", color = TextSecondary, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showDevicesDialog = false }) { Text("Tutup", color = CyanAccent) }
            }
        )
    }
}

