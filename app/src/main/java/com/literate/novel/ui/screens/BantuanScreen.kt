package com.literate.novel.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.ui.components.LiterateIcons
import com.literate.novel.ui.components.SectionHeader
import com.literate.novel.ui.theme.*

@Composable
fun BantuanScreen(
    onBackClick: () -> Unit = {},
    onShowMessage: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedIndex by remember { mutableStateOf<Int?>(0) } // Default first item is expanded as in mockup!
    var showSupportDialog by remember { mutableStateOf(false) }
    var legalDialogType by remember { mutableStateOf<String?>(null) }

    val allFaqItems = listOf(
        Pair(
            "Bagaimana cara memberi rating?",
            "Buka detail novel, lalu pilih Beri Rating. Tentukan nilai dan tulis ulasan untuk membagikan pengalaman membaca kamu."
        ),
        Pair(
            "Bagaimana menyimpan novel?",
            "Pilih tombol Bookmark pada halaman detail novel untuk menyimpannya ke dalam rak buku digitalmu (Ingin Dibaca, Sedang Dibaca, dsb)."
        ),
        Pair(
            "Apa arti status Tamat dan Ongoing?",
            "Status Tamat menandakan novel sudah tamat secara resmi di sumber aslinya. Sedangkan Ongoing menandakan bahwa novel masih terus berlanjut dan merilis bab-bab baru."
        ),
        Pair(
            "Bagaimana mengubah informasi akun?",
            "Masuk ke tab Profil, pilih Informasi Akun di bawah Pengaturan Akun. Kamu dapat mengubah nama lengkap, nama pengguna, email, dan bio kamu."
        )
    )

    val faqItems = remember(searchQuery) {
        if (searchQuery.isBlank()) allFaqItems
        else allFaqItems.filter {
            it.first.contains(searchQuery, ignoreCase = true) ||
                it.second.contains(searchQuery, ignoreCase = true)
        }
    }

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
                text = "Bantuan & Tentang",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Temukan jawaban dan kenali LITERATE.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Search Box
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Cari topik bantuan...",
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
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = CardBorder,
                    unfocusedBorderColor = CardBorder
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Section: Pertanyaan Umum
        item {
            SectionHeader(title = "Pertanyaan Umum")
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(faqItems.size) { index ->
            val (question, answer) = faqItems[index]
            val isExpanded = expandedIndex == index

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expandedIndex = if (isExpanded) null else index },
                color = CardDark,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = question,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else LiterateIcons.ChevronRight,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = answer,
                            color = Color(0xFFD1D5DB),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 5. Section: Butuh Bantuan Lain?
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(title = "Butuh Bantuan Lain?")
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ceritakan kendalamu",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sertakan penjelasan agar tim kami bisa membantu.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showSupportDialog = true },
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
                            text = "Hubungi Dukungan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 6. Section: Tentang LITERATE
        item {
            SectionHeader(title = "Tentang LITERATE")
            Spacer(modifier = Modifier.height(10.dp))

            // Main Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LITERATE",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Temukan novel, bagikan rating, dan simpan cerita favorit dalam satu tempat.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Syarat & Ketentuan
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { legalDialogType = "terms" },
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
                    Text(
                        text = "Syarat & Ketentuan",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = LiterateIcons.ChevronRight,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Kebijakan Privasi
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { legalDialogType = "privacy" },
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
                    Text(
                        text = "Kebijakan Privasi",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = LiterateIcons.ChevronRight,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showSupportDialog) {
        var issueText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            containerColor = CardDark,
            title = { Text("Hubungi Dukungan LITERATE", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Jelaskan kendala atau saran kamu:", color = TextSecondary, fontSize = 12.sp)
                    OutlinedTextField(
                        value = issueText,
                        onValueChange = { issueText = it },
                        placeholder = { Text("Tulis pesan kendalamu di sini...", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
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
                    if (issueText.isNotBlank()) {
                        showSupportDialog = false
                        onShowMessage("Laporan kendala terkirim ke tim LITERATE!")
                    } else {
                        onShowMessage("Mohon isi pesan kendala terlebih dahulu.")
                    }
                }) { Text("Kirim", color = MintPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showSupportDialog = false }) { Text("Batal", color = TextSecondary) }
            }
        )
    }

    if (legalDialogType != null) {
        val isTerms = legalDialogType == "terms"
        AlertDialog(
            onDismissRequest = { legalDialogType = null },
            containerColor = CardDark,
            title = {
                Text(
                    if (isTerms) "Syarat & Ketentuan" else "Kebijakan Privasi",
                    color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    if (isTerms) {
                        "LITERATE adalah platform katalog, pelacakan bacaan, rating, dan komunitas ulasan novel. Pengguna wajib menggunakan fitur Spoiler Tag saat membahas alur penting dan dilarang melakukan spam ulasan."
                    } else {
                        "Data profil, koleksi rak buku digital, dan ulasan kamu disimpan untuk pengalaman komunitas LITERATE. Kamu dapat mengatur visibilitas profil kapan saja melalui menu Privasi."
                    },
                    color = TextSecondary, fontSize = 13.sp, lineHeight = 19.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { legalDialogType = null }) { Text("Mengerti", color = MintPrimary, fontWeight = FontWeight.Bold) }
            }
        )
    }
}

