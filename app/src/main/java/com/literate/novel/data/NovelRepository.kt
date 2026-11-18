package com.literate.novel.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object NovelRepository {

    // All cover URLs are sourced directly from https://www.novelupdates.com / cdn.novelupdates.com
    private val initialNovels = listOf(
        Novel(
            id = "lotm",
            title = "Lord of The Mysteries",
            coverUrl = "https://cdn.novelupdates.com/images/2018/11/Lord-of-the-Mysteries.jpeg",
            rating = 4.7,
            totalVotes = 2421,
            status = "Tamat",
            chapters = 1432,
            genres = listOf("Drama", "Horror", "Mature", "Mystery", "Psychological", "Seinen", "Supranatural"),
            author = "Cuttlefish That Loves Diving",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2018,
            synopsis = "Di dunia era uap yang dipenuhi mesin, ritual, dan kekuatan gaib, Zhou Mingrui terbangun sebagai Klein Moretti. Pencarian jawaban membawanya menelusuri rahasia para Beyonder, organisasi tersembunyi, dan dewa-dewi kuno. Untuk bertahan hidup, ia harus mengungkap misteri di balik dunia yang kini menjadi rumahnya.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Transmigration", "2.4k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: The Clown Potion", "1.8k reads", "10 min read"),
                ChapterItem(3, "Chapter 3: Sequence 9 — Seer", "1.5k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev1",
                    userName = "Hendrik Nurcahyo",
                    avatarColor = 0xFF8B5CF6,
                    stars = 5.0f,
                    content = "Novel terbaik yang pernah kubaca! Dunia building-nya luar biasa detail dan karakternya sangat kompleks."
                ),
                ReviewItem(
                    id = "rev2",
                    userName = "KleinFanatic",
                    avatarColor = 0xFFD97706,
                    stars = 4.0f,
                    content = "Plotnya sangat misterius dan unpredictable. Sangat direkomendasikan untuk pecinta dark fantasy!"
                )
            ),
            readingProgress = ReadingProgress(
                currentChapter = 358,
                totalChapters = 1432,
                lastReadText = "Terakhir dibaca hari ini"
            ),
            bookshelf = BookshelfType.CURRENTLY_READING,
            isWeeklyPopular = true,
            weeklyRank = 1
        ),
        Novel(
            id = "reverend",
            title = "Reverend Insanity",
            coverUrl = "https://cdn.novelupdates.com/images/2025/06/Reverend-Insanity.jpg",
            rating = 4.8,
            totalVotes = 1890,
            status = "Tamat",
            chapters = 1621,
            genres = listOf("Xianxia", "Dark", "Mystery"),
            author = "Gu Zhen Ren",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2012,
            synopsis = "Humans are clever in tens of thousands of ways; Gu are the true refined essences of Heaven and Earth. Fang Yuan terbangun kembali ke 500 tahun lalu menggunakan Spring Autumn Cicada, mengejar keabadian tanpa kompromi.",
            readingProgress = null,
            bookshelf = BookshelfType.WANT_TO_READ
        ),
        Novel(
            id = "mechanic",
            title = "The Legendary Mechanic",
            coverUrl = "https://cdn.novelupdates.com/images/2018/11/legemecha1.jpg",
            rating = 4.6,
            totalVotes = 1520,
            status = "Tamat",
            chapters = 1463,
            genres = listOf("Sci-Fi", "Action", "Reincarnation"),
            author = "Chocolion",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2017,
            synopsis = "Han Xiao, seorang power leveler profesional terbangun di dalam game kesukaannya sebagai seorang NPC sebelum game tersebut resmi diluncurkan. Menggunakan pengetahuannya, ia bersiap menghadapi kedatangan pemain.",
            readingProgress = null,
            bookshelf = BookshelfType.WANT_TO_READ
        ),
        Novel(
            id = "magus",
            title = "Warlock of the Magus World",
            coverUrl = "https://cdn.novelupdates.com/images/2022/05/Warlock-of-the-Magus-World_1653417756.jpg",
            rating = 4.5,
            totalVotes = 1340,
            status = "Tamat",
            chapters = 1200,
            genres = listOf("Sci-Fi", "Dark", "Fantasy"),
            author = "The Plagiarist",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2015,
            synopsis = "Ilmuwan masa depan bereinkarnasi di dunia sihir dengan A.I. Chip yang tertanam di jiwanya. Dingin dan analitis, Leylin meneliti garis keturunan mistis demi mengejar kekuatan mutlak.",
            readingProgress = ReadingProgress(
                currentChapter = 1200,
                totalChapters = 1200,
                lastReadText = "1200 / 1200 chapters · 100% selesai"
            ),
            bookshelf = BookshelfType.READ
        ),
        Novel(
            id = "eternal",
            title = "A Will Eternal",
            coverUrl = "https://cdn.novelupdates.com/images/2016/06/betacover.jpg",
            rating = 4.4,
            totalVotes = 1210,
            status = "Ongoing",
            chapters = 1314,
            genres = listOf("Xianxia", "Comedy", "Action"),
            author = "Er Gen",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2016,
            synopsis = "Satu kehendak menciptakan samudera, satu kehendak memusnahkan iblis. Bai Xiaochun yang sangat takut mati berjuang mencapai keabadian melalui aksi kocak dan dedikasi luar biasa.",
            readingProgress = ReadingProgress(
                currentChapter = 105,
                totalChapters = 1314,
                lastReadText = "Terakhir dibaca kemarin"
            ),
            bookshelf = BookshelfType.CURRENTLY_READING
        )
    )

    var novels by mutableStateOf(initialNovels)
        private set

    var userProfile by mutableStateOf(
        UserProfile(
            name = "Pembaca Novel",
            username = "@pembacanovel",
            email = "pembaca@example.com",
            bio = "Mencari cerita yang sulit dilupakan.",
            totalCollections = 5,
            totalReading = 2,
            totalFinished = 1,
            favoriteGenres = listOf("Mystery", "Fantasy", "Psychological")
        )
    )

    var notificationSettings by mutableStateOf(NotificationSettings())
    var privacySettings by mutableStateOf(PrivacySettings())

    var myReview by mutableStateOf(
        ReviewItem(
            id = "my_lotm_review",
            userName = "Pembaca Novel",
            stars = 5.0f,
            content = "Misterinya bikin penasaran. Dunia dan karakternya terasa hidup."
        )
    )

    fun getNovelById(id: String): Novel? = novels.find { it.id == id }

    fun updateBookshelf(novelId: String, shelf: BookshelfType?) {
        novels = novels.map {
            if (it.id == novelId) it.copy(bookshelf = shelf) else it
        }
    }

    fun addReview(novelId: String, stars: Float, content: String, isSpoiler: Boolean, progressTag: String?) {
        val newRev = ReviewItem(
            id = "rev_${System.currentTimeMillis()}",
            userName = userProfile.name,
            stars = stars,
            content = content,
            isSpoiler = isSpoiler,
            progressTag = progressTag
        )
        novels = novels.map {
            if (it.id == novelId) {
                val updatedReviews = listOf(newRev) + it.reviews
                val newVotes = it.totalVotes + 1
                val newAvg = ((it.rating * it.totalVotes) + stars) / newVotes
                it.copy(
                    reviews = updatedReviews,
                    totalVotes = newVotes,
                    rating = String.format("%.1f", newAvg).toDoubleOrNull() ?: it.rating
                )
            } else it
        }
        if (novelId == "lotm") {
            myReview = newRev
        }
    }

    fun updateUserProfile(name: String, username: String, email: String, bio: String) {
        userProfile = userProfile.copy(
            name = name,
            username = username,
            email = email,
            bio = bio
        )
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        notificationSettings = settings
    }

    fun updatePrivacySettings(settings: PrivacySettings) {
        privacySettings = settings
    }
}

