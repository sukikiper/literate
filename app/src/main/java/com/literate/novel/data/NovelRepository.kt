package com.literate.novel.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object NovelRepository {

    // All 12 novels & cover URLs sourced directly from https://www.novelupdates.com / cdn.novelupdates.com
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
            genres = listOf("Xianxia", "Dark", "Mystery", "Action", "Fantasy"),
            author = "Gu Zhen Ren",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2012,
            synopsis = "Manusia memiliki ribuan cara untuk menjadi cerdik, sedangkan Gu adalah inti sejati dari Langit dan Bumi. Fang Yuan, seorang kultivator berusia 500 tahun, menggunakan Spring Autumn Cicada untuk kembali ke masa mudanya dan mengejar keabadian tanpa terikat moralitas konvensional.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Heart of a Demon Never Has Regret", "3.1k reads", "7 min read"),
                ChapterItem(2, "Chapter 2: Spring Autumn Cicada", "2.6k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: Playing with a Youth", "2.1k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_ri1",
                    userName = "FangYuanEnjoyer",
                    avatarColor = 0xFFEF4444,
                    stars = 5.0f,
                    content = "Protagonis paling rasional dan tak terduga di dunia web novel. Sistem kekuatan Gu sangat unik!"
                ),
                ReviewItem(
                    id = "rev_ri2",
                    userName = "DaoSeeker",
                    avatarColor = 0xFF10B981,
                    stars = 5.0f,
                    content = "Filosofi dan strategi politik antar klan di sini benar-benar di level berbeda."
                )
            ),
            readingProgress = null,
            bookshelf = BookshelfType.WANT_TO_READ,
            isWeeklyPopular = true,
            weeklyRank = 2
        ),
        Novel(
            id = "mechanic",
            title = "The Legendary Mechanic",
            coverUrl = "https://cdn.novelupdates.com/images/2018/11/legemecha1.jpg",
            rating = 4.6,
            totalVotes = 1520,
            status = "Tamat",
            chapters = 1463,
            genres = listOf("Sci-Fi", "Action", "Reincarnation", "Comedy"),
            author = "Chocolion",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2017,
            synopsis = "Han Xiao, seorang power leveler profesional, terbangun di dalam game galaksi kesukaannya sebagai seorang NPC sebelum game tersebut resmi diluncurkan. Dengan pengetahuan masa depannya, ia membangun faksi mekanik raksasa dan bersiap menyambut kedatangan para pemain.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Germinal Organization", "1.9k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: Basic Assembly", "1.6k reads", "7 min read"),
                ChapterItem(3, "Chapter 3: Escape from the Lab", "1.4k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_tlm1",
                    userName = "BlackStarFan",
                    avatarColor = 0xFF3B82F6,
                    stars = 5.0f,
                    content = "Interaksi Han Xiao dengan para player kocak banget! World building sci-fi-nya juga epik."
                ),
                ReviewItem(
                    id = "rev_tlm2",
                    userName = "MechaReader",
                    avatarColor = 0xFF06B6D4,
                    stars = 4.5f,
                    content = "Salah satu novel sci-fi + game elements terbaik dari awal sampai ending."
                )
            ),
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
            genres = listOf("Sci-Fi", "Dark", "Fantasy", "Action"),
            author = "The Plagiarist",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2015,
            synopsis = "Seorang ilmuwan dari era futuristik bereinkarnasi ke dunia sihir abad pertengahan dengan A.I. Chip canggih yang menyatu di dalam jiwanya. Dingin, kalkulatif, dan pragmatis, Leylin Farlier meneliti rahasia Magi dan garis keturunan makhluk mistis demi meraih keabadian.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: Leylin Farlier", "1.7k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: The A.I. Chip", "1.5k reads", "7 min read"),
                ChapterItem(3, "Chapter 3: Abyssal Bone Forest", "1.3k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_wmw1",
                    userName = "LeylinID",
                    avatarColor = 0xFF7C3AED,
                    stars = 4.5f,
                    content = "Kombinasi logika ilmiah dengan dunia sihir gelap terasa sangat memuaskan."
                ),
                ReviewItem(
                    id = "rev_wmw2",
                    userName = "DarkMage99",
                    avatarColor = 0xFF6366F1,
                    stars = 4.5f,
                    content = "Pacing stabil dan ending-nya mantap. Wajib baca kalau suka MC realistis."
                )
            ),
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
            genres = listOf("Xianxia", "Comedy", "Action", "Fantasy"),
            author = "Er Gen",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2016,
            synopsis = "Satu kehendak menciptakan samudera, satu kehendak memusnahkan iblis. Bai Xiaochun, pemuda yang sangat takut mati dan terobsesi hidup abadi, menempuh jalan kultivasi penuh kekacauan kocak namun tetap menjunjung tinggi persahabatan.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: I am Bai Xiaochun", "2.0k reads", "7 min read"),
                ChapterItem(2, "Chapter 2: The Ovens", "1.7k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: Spirit Stream Sect", "1.4k reads", "8 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_awe1",
                    userName = "BaiXiaochunFans",
                    avatarColor = 0xFFF59E0B,
                    stars = 5.0f,
                    content = "Komedinya bikin ngakak terus, tapi saat arc perang tetap merinding dan emosional!"
                ),
                ReviewItem(
                    id = "rev_awe2",
                    userName = "ErGenLover",
                    avatarColor = 0xFF14B8A6,
                    stars = 4.0f,
                    content = "Xianxia paling ringan dan menghibur karya Er Gen."
                )
            ),
            readingProgress = ReadingProgress(
                currentChapter = 105,
                totalChapters = 1314,
                lastReadText = "Terakhir dibaca kemarin"
            ),
            bookshelf = BookshelfType.CURRENTLY_READING
        ),
        Novel(
            id = "orv",
            title = "Omniscient Reader's Viewpoint",
            coverUrl = "https://cdn.novelupdates.com/images/2018/07/orv.jpg",
            rating = 4.9,
            totalVotes = 3150,
            status = "Tamat",
            chapters = 551,
            genres = listOf("Action", "Fantasy", "Mystery", "Psychological"),
            author = "Sing-Shong",
            originalLanguage = "Korean",
            releaseYear = 2018,
            synopsis = "Kim Dokja adalah satu-satunya pembaca setia web novel 'Three Ways to Survive the Apocalypse' selama sepuluh tahun. Ketika dunia nyata tiba-tiba berubah menjadi skenario mematikan persis seperti isi novel tersebut, hanya Dokja yang mengetahui akhir ceritanya.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: Prologue — Three Ways to Survive", "4.2k reads", "9 min read"),
                ChapterItem(2, "Chapter 2: Starting the Paid Service", "3.6k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: The First Scenario", "3.2k reads", "10 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_orv1",
                    userName = "DokjaCompany",
                    avatarColor = 0xFF0EA5E9,
                    stars = 5.0f,
                    content = "Masterpiece Korea! Epilognya benar-benar membekas di hati dan bikin susah move on."
                ),
                ReviewItem(
                    id = "rev_orv2",
                    userName = "ConstellationReader",
                    avatarColor = 0xFF6366F1,
                    stars = 5.0f,
                    content = "Konsep rasi bintang dan meta-narasi tentang pembaca & penulis sangat jenius."
                )
            ),
            isWeeklyPopular = true,
            weeklyRank = 3
        ),
        Novel(
            id = "sololeveling",
            title = "Solo Leveling",
            coverUrl = "https://cdn.novelupdates.com/images/2019/01/Solo-Leveling.jpg",
            rating = 4.6,
            totalVotes = 2890,
            status = "Tamat",
            chapters = 270,
            genres = listOf("Action", "Fantasy", "Dark"),
            author = "Chu-Gong",
            originalLanguage = "Korean",
            releaseYear = 2016,
            synopsis = "Sung Jin-Woo dikenal sebagai Hunter Rank-E terlemah di dunia. Setelah nyaris tewas di dalam Double Dungeon rahasia, ia mendapatkan 'System' misterius yang memungkinkannya naik level tanpa batas dan membangkitkan pasukan bayangan.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Weakest Hunter", "3.8k reads", "7 min read"),
                ChapterItem(2, "Chapter 2: The Double Dungeon", "3.3k reads", "9 min read"),
                ChapterItem(3, "Chapter 3: Player Qualification", "2.9k reads", "8 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_sl1",
                    userName = "ShadowMonarch",
                    avatarColor = 0xFF8B5CF6,
                    stars = 5.0f,
                    content = "Aksi cepat, hype tiada henti, dan momen 'Arise' selalu bikin merinding!"
                ),
                ReviewItem(
                    id = "rev_sl2",
                    userName = "HunterGuild",
                    avatarColor = 0xFFEC4899,
                    stars = 4.5f,
                    content = "Cocok banget buat pembaca baru yang suka power fantasy penuh adrenalin."
                )
            )
        ),
        Novel(
            id = "tcf",
            title = "Trash of the Count's Family",
            coverUrl = "https://cdn.novelupdates.com/images/2022/10/Trash-of-the-Counts-Family.jpg",
            rating = 4.7,
            totalVotes = 1980,
            status = "Ongoing",
            chapters = 776,
            genres = listOf("Fantasy", "Action", "Comedy", "Adventure"),
            author = "Yoo Ryeo Han",
            originalLanguage = "Korean",
            releaseYear = 2018,
            synopsis = "Kim Rok Soo terbangun di dalam tubuh Cale Henituse, bangsawan pembuat onar dari novel 'The Birth of a Hero'. Impian Cale sangat sederhana: hidup santai sebagai orang kaya tanpa terlibat perang. Namun rencananya justru menjadikannya pahlawan terbesar kerajaan.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: Waking Up as Cale Henituse", "2.3k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: The Indestructible Shield", "1.9k reads", "9 min read"),
                ChapterItem(3, "Chapter 3: Meeting Choi Han", "1.7k reads", "8 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_tcf1",
                    userName = "SlackerLife",
                    avatarColor = 0xFFEF4444,
                    stars = 5.0f,
                    content = "Found family terbaik! Interaksi Cale dengan Raon, On, dan Hong sangat menggemaskan."
                ),
                ReviewItem(
                    id = "rev_tcf2",
                    userName = "CaleHenituse",
                    avatarColor = 0xFF10B981,
                    stars = 4.5f,
                    content = "Niatnya cuma mau rebahan, malah menyelamatkan benua berkali-kali."
                )
            )
        ),
        Novel(
            id = "rtw",
            title = "Release That Witch",
            coverUrl = "https://cdn.novelupdates.com/images/2024/08/Release-that-Witch.jpg",
            rating = 4.6,
            totalVotes = 1760,
            status = "Tamat",
            chapters = 1498,
            genres = listOf("Fantasy", "Sci-Fi", "Action"),
            author = "Er Mu",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2016,
            synopsis = "Chen Yan, seorang insinyur mesin modern, bereinkarnasi menjadi Pangeran Keempat Roland Wimbledon di dunia abad pertengahan tempat para penyihir diburu. Ia menyelamatkan para penyihir dan menggabungkan sihir mereka dengan teknologi industri modern.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Witch's Execution", "2.1k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: Anna's Green Flame", "1.8k reads", "9 min read"),
                ChapterItem(3, "Chapter 3: Steam Engine Blueprint", "1.6k reads", "8 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_rtw1",
                    userName = "BorderTownMayor",
                    avatarColor = 0xFF3B82F6,
                    stars = 5.0f,
                    content = "Kingdom building terbaik! Revolusi industri melawan monster iblis terasa sangat logis."
                ),
                ReviewItem(
                    id = "rev_rtw2",
                    userName = "SteamEngineer",
                    avatarColor = 0xFFF59E0B,
                    stars = 4.5f,
                    content = "Pengembangan kota dan karakter para penyihir ditulis dengan penuh perhatian."
                )
            )
        ),
        Novel(
            id = "tne",
            title = "The Novel's Extra",
            coverUrl = "https://cdn.novelupdates.com/images/2018/08/TheNovelsExtra.jpg",
            rating = 4.5,
            totalVotes = 1640,
            status = "Tamat",
            chapters = 398,
            genres = listOf("Action", "Fantasy", "Mystery", "Comedy"),
            author = "Jee Gab Song",
            originalLanguage = "Korean",
            releaseYear = 2018,
            synopsis = "Kim Hajin terbangun di dalam novel yang ia tulis sendiri—bukan sebagai tokoh utama, melainkan karakter figuran tanpa nama. Ia harus bertahan hidup di Akademi Cube sembari menyadari bahwa alur ceritanya telah diubah oleh sosok misterius.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: Inside My Own Novel", "1.9k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: Cube Academy", "1.6k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: The Co-Author", "1.5k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_tne1",
                    userName = "ChameleonTroupe",
                    avatarColor = 0xFF14B8A6,
                    stars = 4.5f,
                    content = "Arc akademinya legendaris! Gaya bertarung sniper Hajin juga beda dari MC biasa."
                ),
                ReviewItem(
                    id = "rev_tne2",
                    userName = "NovelWatcher",
                    avatarColor = 0xFF8B5CF6,
                    stars = 4.5f,
                    content = "Premis penulis masuk ke dunianya sendiri dieksekusi dengan sangat seru."
                )
            )
        ),
        Novel(
            id = "scog",
            title = "The Second Coming of Gluttony",
            coverUrl = "https://cdn.novelupdates.com/images/2019/01/The-Second-Coming-of-Avarice.jpg",
            rating = 4.7,
            totalVotes = 1830,
            status = "Tamat",
            chapters = 489,
            genres = listOf("Action", "Fantasy", "Dark", "Psychological"),
            author = "Ro Yu-jin",
            originalLanguage = "Korean",
            releaseYear = 2017,
            synopsis = "Setelah menghancurkan hidupnya sendiri dan menyesali segalanya di ambang kematian, Seol Jihu diberi kesempatan kedua untuk mengirim perasaannya ke masa lalu. Berbekal mata sembilan warna, ia memasuki dunia Paradise untuk menebus dosanya.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: The Last Wish", "2.2k reads", "9 min read"),
                ChapterItem(2, "Chapter 2: Nine Eyes", "1.9k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: Tutorial Zone", "1.7k reads", "10 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_scog1",
                    userName = "ParadiseSpear",
                    avatarColor = 0xFFD97706,
                    stars = 5.0f,
                    content = "Perkembangan karakter Seol Jihu dari titik terendah hingga jadi pahlawan sangat menyentuh."
                ),
                ReviewItem(
                    id = "rev_scog2",
                    userName = "GluttonyReader",
                    avatarColor = 0xFFEF4444,
                    stars = 4.5f,
                    content = "Adegan pertempuran dan ketegangannya ditulis sangat intens!"
                )
            )
        ),
        Novel(
            id = "ri_xianni",
            title = "Renegade Immortal",
            coverUrl = "https://cdn.novelupdates.com/images/2016/03/xianni-1.jpg",
            rating = 4.6,
            totalVotes = 1490,
            status = "Tamat",
            chapters = 2088,
            genres = listOf("Xianxia", "Action", "Dark", "Fantasy"),
            author = "Er Gen",
            originalLanguage = "Chinese (Mandarin)",
            releaseYear = 2009,
            synopsis = "Wang Lin adalah pemuda biasa dengan bakat kultivasi pas-pasan yang menemukan manik misterius pengubah takdir. Menentang langit dan nasib, ia menapaki jalan keabadian yang sunyi, brutal, dan penuh pengorbanan demi melindungi orang-orang yang dicintainya.",
            chaptersList = listOf(
                ChapterItem(1, "Chapter 1: Leaving Home", "1.8k reads", "8 min read"),
                ChapterItem(2, "Chapter 2: Heng Yue Sect", "1.5k reads", "8 min read"),
                ChapterItem(3, "Chapter 3: The Heaven Defying Bead", "1.4k reads", "9 min read")
            ),
            reviews = listOf(
                ReviewItem(
                    id = "rev_xn1",
                    userName = "WangLinDao",
                    avatarColor = 0xFF6366F1,
                    stars = 5.0f,
                    content = "Karya klasik xianxia yang emosional dan dalam. Arc mortal Wang Lin tak tertandingi."
                ),
                ReviewItem(
                    id = "rev_xn2",
                    userName = "ImmortalPath",
                    avatarColor = 0xFF0EA5E9,
                    stars = 4.5f,
                    content = "Atmosfer gelap dan tekad Wang Lin membuat novel ini tetap relevan sampai sekarang."
                )
            )
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
