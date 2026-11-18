package com.literate.novel.data

data class ChapterItem(
    val number: Int,
    val title: String,
    val reads: String,
    val readTime: String
)

data class ReviewItem(
    val id: String,
    val userName: String,
    val avatarColor: Long = 0xFF6366F1,
    val stars: Float,
    val content: String,
    val isSpoiler: Boolean = false,
    val progressTag: String? = null,
    val helpfulCount: Int = 0
)

data class ReadingProgress(
    val currentChapter: Int,
    val totalChapters: Int,
    val lastReadText: String
) {
    val percentage: Int
        get() = if (totalChapters > 0) ((currentChapter.toFloat() / totalChapters) * 100).toInt() else 0
}

enum class BookshelfType(val label: String) {
    WANT_TO_READ("Ingin Dibaca"),
    CURRENTLY_READING("Sedang Dibaca"),
    READ("Selesai Dibaca"),
    DNF("Did Not Finish (DNF)")
}

data class Novel(
    val id: String,
    val title: String,
    val coverUrl: String,
    val rating: Double,
    val totalVotes: Int,
    val status: String, // "Tamat" or "Ongoing"
    val chapters: Int,
    val genres: List<String>,
    val author: String,
    val originalLanguage: String,
    val releaseYear: Int,
    val synopsis: String,
    val chaptersList: List<ChapterItem> = emptyList(),
    val reviews: List<ReviewItem> = emptyList(),
    val readingProgress: ReadingProgress? = null,
    val bookshelf: BookshelfType? = null,
    val isWeeklyPopular: Boolean = false,
    val weeklyRank: Int? = null
)

data class UserProfile(
    val name: String,
    val username: String,
    val email: String,
    val bio: String,
    val totalCollections: Int,
    val totalReading: Int,
    val totalFinished: Int,
    val favoriteGenres: List<String>
)

data class NotificationSettings(
    val appNotification: Boolean = true,
    val emailNotification: Boolean = false,
    val newChapter: Boolean = true,
    val reviewReply: Boolean = true,
    val novelRecommendation: Boolean = false,
    val readingReminder: Boolean = true,
    val quietTimeEnabled: Boolean = true,
    val quietTimeRange: String = "22.00 – 07.00"
)

data class PrivacySettings(
    val profileVisibility: String = "Publik", // "Publik" or "Privat"
    val showLibrary: Boolean = false,
    val showReadingProgress: Boolean = false,
    val showRatingAndReview: Boolean = true
)

data class FaqItem(
    val question: String,
    val answer: String,
    val isExpanded: Boolean = false
)

