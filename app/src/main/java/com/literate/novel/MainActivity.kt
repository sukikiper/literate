package com.literate.novel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.Novel
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.LiterateBottomBar
import com.literate.novel.ui.components.LiterateTopBar
import com.literate.novel.ui.components.NavigationTab
import com.literate.novel.ui.dialogs.AuthDialog
import com.literate.novel.ui.dialogs.BookmarkDialog
import com.literate.novel.ui.dialogs.RatingDialog
import com.literate.novel.ui.screens.*
import com.literate.novel.ui.theme.*
import kotlinx.coroutines.launch

enum class AppSubScreen {
    NONE,
    INFORMASI_AKUN,
    NOTIFIKASI,
    PRIVASI,
    BANTUAN
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiterateTheme {
                LiterateApp()
            }
        }
    }
}

@Composable
fun LiterateApp() {
    var currentTab by remember { mutableStateOf(NavigationTab.BERANDA) }
    var currentSubScreen by remember { mutableStateOf(AppSubScreen.NONE) }
    var selectedNovelId by remember { mutableStateOf("lotm") }
    var selectedJelajahGenre by remember { mutableStateOf("Semua") }

    // Dialog states
    var ratingNovelTarget by remember { mutableStateOf<Novel?>(null) }
    var bookmarkNovelTarget by remember { mutableStateOf<Novel?>(null) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var isSignUpDialog by remember { mutableStateOf(false) }
    var showMenuDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showMessage: (String) -> Unit = { msg ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            LiterateTopBar(
                onMenuClick = { showMenuDialog = true },
                onSearchClick = {
                    selectedJelajahGenre = "Semua"
                    currentTab = NavigationTab.CARI
                    currentSubScreen = AppSubScreen.NONE
                },
                onSignInClick = {
                    isSignUpDialog = false
                    showAuthDialog = true
                },
                onSignUpClick = {
                    isSignUpDialog = true
                    showAuthDialog = true
                }
            )
        },
        bottomBar = {
            LiterateBottomBar(
                currentTab = currentTab,
                onTabSelected = { tab ->
                    currentTab = tab
                    currentSubScreen = AppSubScreen.NONE
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BgDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BgDark)
        ) {
            when {
                // Profile Sub-Screens
                currentSubScreen == AppSubScreen.INFORMASI_AKUN -> {
                    InformasiAkunScreen(
                        onBackClick = { currentSubScreen = AppSubScreen.NONE },
                        onShowMessage = showMessage
                    )
                }
                currentSubScreen == AppSubScreen.NOTIFIKASI -> {
                    NotifikasiScreen(
                        onBackClick = { currentSubScreen = AppSubScreen.NONE },
                        onShowMessage = showMessage
                    )
                }
                currentSubScreen == AppSubScreen.PRIVASI -> {
                    PrivasiScreen(
                        onBackClick = { currentSubScreen = AppSubScreen.NONE },
                        onShowMessage = showMessage
                    )
                }
                currentSubScreen == AppSubScreen.BANTUAN -> {
                    BantuanScreen(
                        onBackClick = { currentSubScreen = AppSubScreen.NONE },
                        onShowMessage = showMessage
                    )
                }
                // Main Navigation Tabs
                currentTab == NavigationTab.BERANDA -> {
                    DetailNovelScreen(
                        novelId = selectedNovelId,
                        onNovelClick = { id -> selectedNovelId = id },
                        onRateNowClick = { novel -> ratingNovelTarget = novel },
                        onBookmarkClick = { novel -> bookmarkNovelTarget = novel },
                        onShowMessage = showMessage
                    )
                }
                currentTab == NavigationTab.CARI -> {
                    JelajahScreen(
                        initialGenre = selectedJelajahGenre,
                        onNovelClick = { id ->
                            selectedNovelId = id
                            currentTab = NavigationTab.BERANDA
                        }
                    )
                }
                currentTab == NavigationTab.PERPUSTAKAAN -> {
                    PerpustakaanScreen(
                        onNovelClick = { id ->
                            selectedNovelId = id
                            currentTab = NavigationTab.BERANDA
                        },
                        onReviewClick = { novel -> ratingNovelTarget = novel },
                        onBookmarkClick = { novel -> bookmarkNovelTarget = novel },
                        onManageSavedClick = { showMessage("Daftar bacaan tersimpan") }
                    )
                }
                currentTab == NavigationTab.PROFIL -> {
                    ProfilScreen(
                        onNavigateToAccountInfo = { currentSubScreen = AppSubScreen.INFORMASI_AKUN },
                        onNavigateToNotification = { currentSubScreen = AppSubScreen.NOTIFIKASI },
                        onNavigateToPrivacy = { currentSubScreen = AppSubScreen.PRIVASI },
                        onNavigateToHelp = { currentSubScreen = AppSubScreen.BANTUAN },
                        onNovelClick = { id ->
                            selectedNovelId = id
                            currentTab = NavigationTab.BERANDA
                        },
                        onGenreClick = { genre ->
                            selectedJelajahGenre = genre
                            currentTab = NavigationTab.CARI
                            currentSubScreen = AppSubScreen.NONE
                        },
                        onEditReviewClick = {
                            ratingNovelTarget = NovelRepository.getNovelById("lotm")
                        },
                        onShowMessage = showMessage
                    )
                }
            }
        }
    }

    // Quick Navigation Menu Dialog (Hamburger button)
    if (showMenuDialog) {
        val menuItems = listOf(
            "Beranda" to { currentTab = NavigationTab.BERANDA; currentSubScreen = AppSubScreen.NONE },
            "Jelajah Novel" to { selectedJelajahGenre = "Semua"; currentTab = NavigationTab.CARI; currentSubScreen = AppSubScreen.NONE },
            "Perpustakaan" to { currentTab = NavigationTab.PERPUSTAKAAN; currentSubScreen = AppSubScreen.NONE },
            "Profil" to { currentTab = NavigationTab.PROFIL; currentSubScreen = AppSubScreen.NONE },
            "Informasi Akun" to { currentTab = NavigationTab.PROFIL; currentSubScreen = AppSubScreen.INFORMASI_AKUN },
            "Notifikasi" to { currentTab = NavigationTab.PROFIL; currentSubScreen = AppSubScreen.NOTIFIKASI },
            "Privasi" to { currentTab = NavigationTab.PROFIL; currentSubScreen = AppSubScreen.PRIVASI },
            "Bantuan & Tentang" to { currentTab = NavigationTab.PROFIL; currentSubScreen = AppSubScreen.BANTUAN }
        )
        AlertDialog(
            onDismissRequest = { showMenuDialog = false },
            containerColor = CardDark,
            title = {
                Text("Menu LITERATE", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    menuItems.forEach { (label, action) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    action()
                                    showMenuDialog = false
                                },
                            color = InputBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = label,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMenuDialog = false }) {
                    Text("Tutup", color = CyanAccent)
                }
            }
        )
    }

    // Rating Dialog (PRD 4.2)
    val targetNovelForRating = ratingNovelTarget
    if (targetNovelForRating != null) {
        RatingDialog(
            novelTitle = targetNovelForRating.title,
            onDismiss = { ratingNovelTarget = null },
            onSubmit = { stars, content, isSpoiler, progressTag ->
                NovelRepository.addReview(
                    novelId = targetNovelForRating.id,
                    stars = stars,
                    content = content,
                    isSpoiler = isSpoiler,
                    progressTag = progressTag
                )
                showMessage("Ulasan berhasil dikirim!")
            }
        )
    }

    // Bookmark Dialog (PRD 4.3)
    val targetNovelForBookmark = bookmarkNovelTarget
    if (targetNovelForBookmark != null) {
        BookmarkDialog(
            novelTitle = targetNovelForBookmark.title,
            currentShelf = targetNovelForBookmark.bookshelf,
            onDismiss = { bookmarkNovelTarget = null },
            onSelectShelf = { shelf ->
                NovelRepository.updateBookshelf(targetNovelForBookmark.id, shelf)
                showMessage(
                    if (shelf != null) "Disimpan ke rak: ${shelf.label}" else "Dihapus dari rak buku"
                )
            }
        )
    }

    // Auth Dialog
    if (showAuthDialog) {
        AuthDialog(
            isSignUp = isSignUpDialog,
            onDismiss = { showAuthDialog = false },
            onSuccess = showMessage
        )
    }
}
