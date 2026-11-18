package com.literate.novel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.literate.novel.data.BookshelfType
import com.literate.novel.data.Novel
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.LiterateBottomBar
import com.literate.novel.ui.components.LiterateTopBar
import com.literate.novel.ui.components.NavigationTab
import com.literate.novel.ui.dialogs.AuthDialog
import com.literate.novel.ui.dialogs.BookmarkDialog
import com.literate.novel.ui.dialogs.RatingDialog
import com.literate.novel.ui.screens.*
import com.literate.novel.ui.theme.BgDark
import com.literate.novel.ui.theme.LiterateTheme
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

    // Dialog states
    var ratingNovelTarget by remember { mutableStateOf<Novel?>(null) }
    var bookmarkNovelTarget by remember { mutableStateOf<Novel?>(null) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var isSignUpDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            LiterateTopBar(
                onMenuClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Menu LITERATE dibuka")
                    }
                },
                onSearchClick = {
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
                    InformasiAkunScreen(onBackClick = { currentSubScreen = AppSubScreen.NONE })
                }
                currentSubScreen == AppSubScreen.NOTIFIKASI -> {
                    NotifikasiScreen(onBackClick = { currentSubScreen = AppSubScreen.NONE })
                }
                currentSubScreen == AppSubScreen.PRIVASI -> {
                    PrivasiScreen(onBackClick = { currentSubScreen = AppSubScreen.NONE })
                }
                currentSubScreen == AppSubScreen.BANTUAN -> {
                    BantuanScreen(onBackClick = { currentSubScreen = AppSubScreen.NONE })
                }
                // Main Navigation Tabs
                currentTab == NavigationTab.BERANDA -> {
                    DetailNovelScreen(
                        novelId = selectedNovelId,
                        onNovelClick = { id ->
                            selectedNovelId = id
                        },
                        onRateNowClick = { novel ->
                            ratingNovelTarget = novel
                        },
                        onBookmarkClick = { novel ->
                            bookmarkNovelTarget = novel
                        }
                    )
                }
                currentTab == NavigationTab.CARI -> {
                    JelajahScreen(
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
                        onReviewClick = { novel ->
                            ratingNovelTarget = novel
                        },
                        onManageSavedClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Daftar bacaan tersimpan")
                            }
                        }
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
                        onEditReviewClick = {
                            ratingNovelTarget = NovelRepository.getNovelById("lotm")
                        }
                    )
                }
            }
        }
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
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Ulasan berhasil dikirim!")
                }
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
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        if (shelf != null) "Disimpan ke rak: ${shelf.label}" else "Dihapus dari rak buku"
                    )
                }
            }
        )
    }

    // Auth Dialog
    if (showAuthDialog) {
        AuthDialog(
            isSignUp = isSignUpDialog,
            onDismiss = { showAuthDialog = false }
        )
    }
}

