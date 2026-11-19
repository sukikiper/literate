package com.literate.novel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.literate.novel.data.LiterateCache
import com.literate.novel.data.Novel
import com.literate.novel.data.NovelRepository
import com.literate.novel.ui.components.LiterateBottomBar
import com.literate.novel.ui.components.LiterateIcons
import com.literate.novel.ui.components.LiterateTopBar
import com.literate.novel.ui.components.NavigationTab
import com.literate.novel.ui.dialogs.AuthDialog
import com.literate.novel.ui.dialogs.BookmarkDialog
import com.literate.novel.ui.dialogs.RatingDialog
import com.literate.novel.ui.screens.*
import com.literate.novel.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        // Initialize 2-layer RAM + persistent Disk Cache (up to 150 MB)
        LiterateCache.init(applicationContext)
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

    // Sidebar Drawer State
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var cacheSizeLabel by remember { mutableStateOf("0 B") }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showMessage: (String) -> Unit = { msg ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Refresh cache size whenever sidebar opens
    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            cacheSizeLabel = withContext(Dispatchers.IO) {
                LiterateCache.getFormattedCacheSize()
            }
        }
    }

    val navigateFromSidebar: (NavigationTab, AppSubScreen) -> Unit = { tab, sub ->
        if (tab == NavigationTab.CARI) selectedJelajahGenre = "Semua"
        currentTab = tab
        currentSubScreen = sub
        coroutineScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color(0xAA000000),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CardDark,
                drawerContentColor = TextPrimary,
                modifier = Modifier.width(290.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    // Sidebar Brand Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = MintPrimary,
                            shape = RoundedCornerShape(6.dp)
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
                        Text(
                            text = "v1.0.2",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Profile Summary Card in Sidebar
                    Surface(
                        color = InputBg,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.NONE) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(42.dp),
                                shape = CircleShape,
                                color = Color(NovelRepository.avatarColor),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = NovelRepository.avatarText,
                                        color = if (NovelRepository.avatarText == "[ Foto ]") TextSecondary else Color.White,
                                        fontSize = if (NovelRepository.avatarText == "[ Foto ]") 9.sp else 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = NovelRepository.userProfile.name,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = NovelRepository.userProfile.username,
                                    color = CyanAccent,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "MENU UTAMA",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    SidebarNavItem(
                        label = "Beranda",
                        icon = Icons.Default.Home,
                        isSelected = currentTab == NavigationTab.BERANDA && currentSubScreen == AppSubScreen.NONE,
                        onClick = { navigateFromSidebar(NavigationTab.BERANDA, AppSubScreen.NONE) }
                    )
                    SidebarNavItem(
                        label = "Jelajah Novel",
                        icon = Icons.Default.Search,
                        isSelected = currentTab == NavigationTab.CARI && currentSubScreen == AppSubScreen.NONE,
                        onClick = { navigateFromSidebar(NavigationTab.CARI, AppSubScreen.NONE) }
                    )
                    SidebarNavItem(
                        label = "Perpustakaan (${NovelRepository.collectionCount})",
                        icon = LiterateIcons.Bookmark,
                        isSelected = currentTab == NavigationTab.PERPUSTAKAAN && currentSubScreen == AppSubScreen.NONE,
                        onClick = { navigateFromSidebar(NavigationTab.PERPUSTAKAAN, AppSubScreen.NONE) }
                    )
                    SidebarNavItem(
                        label = "Profil",
                        icon = Icons.Default.Person,
                        isSelected = currentTab == NavigationTab.PROFIL && currentSubScreen == AppSubScreen.NONE,
                        onClick = { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.NONE) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = DividerDark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "PENGATURAN",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    SidebarNavItem(
                        label = "Informasi Akun",
                        icon = Icons.Default.Settings,
                        isSelected = currentSubScreen == AppSubScreen.INFORMASI_AKUN,
                        onClick = { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.INFORMASI_AKUN) }
                    )
                    SidebarNavItem(
                        label = "Notifikasi",
                        icon = Icons.Default.Notifications,
                        isSelected = currentSubScreen == AppSubScreen.NOTIFIKASI,
                        onClick = { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.NOTIFIKASI) }
                    )
                    SidebarNavItem(
                        label = "Privasi",
                        icon = Icons.Default.Lock,
                        isSelected = currentSubScreen == AppSubScreen.PRIVASI,
                        onClick = { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.PRIVASI) }
                    )
                    SidebarNavItem(
                        label = "Bantuan & Tentang",
                        icon = Icons.Default.Info,
                        isSelected = currentSubScreen == AppSubScreen.BANTUAN,
                        onClick = { navigateFromSidebar(NavigationTab.PROFIL, AppSubScreen.BANTUAN) }
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = DividerDark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Persistent Cache Status & Clear Button
                    Surface(
                        color = InputBg,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Cache Offline",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = cacheSizeLabel,
                                    color = CyanAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Cover & katalog tersimpan otomatis di memori HP agar loading instan.",
                                color = TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                LiterateCache.clearAll()
                                            }
                                            cacheSizeLabel = "0 B"
                                            drawerState.close()
                                            showMessage("Cache berhasil dibersihkan!")
                                        }
                                    },
                                color = ButtonDarkBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Bersihkan Cache",
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                LiterateTopBar(
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    },
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

@Composable
private fun SidebarNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick),
        color = if (isSelected) InputBg else Color.Transparent,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MintPrimary else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = if (isSelected) MintPrimary else TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
