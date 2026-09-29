package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapters")
data class CachedChapterEntity(
    @PrimaryKey val id: String,
    val gradeLevel: Int,
    val subjectId: String,
    val chapterNumber: Int,
    val title: String,
    val summary: String,
    val readTimeMinutes: Int,
    val fullContent: String,
    val keyConceptsRaw: String, // newline separated or json
    val keyFormulasRaw: String,
    val isBookmarked: Boolean = false,
    val isOfflineSaved: Boolean = true,
    val isCompleted: Boolean = false
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizId: String,
    val title: String,
    val subjectId: String,
    val gradeLevel: Int,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "forum_posts")
data class ForumPostEntity(
    @PrimaryKey val id: String,
    val gradeLevel: Int,
    val subjectId: String,
    val authorName: String,
    val authorRole: String,
    val title: String,
    val content: String,
    val upvotes: Int,
    val replyCount: Int,
    val isSolved: Boolean,
    val isUpvotedByMe: Boolean,
    val tagsRaw: String, // comma separated
    val timestamp: Long,
    val syncStatus: String // "SYNCED" or "LOCAL_DRAFT"
)

@Entity(tableName = "forum_replies")
data class ForumReplyEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val upvotes: Int,
    val isAcceptedSolution: Boolean,
    val timestamp: Long
)

@Entity(tableName = "exam_deadlines")
data class ExamDeadlineEntity(
    @PrimaryKey val id: String,
    val gradeLevel: Int,
    val subjectId: String,
    val title: String,
    val dueTimestamp: Long,
    val reminderHoursBefore: Int,
    val notes: String,
    val isCompleted: Boolean,
    val typeName: String
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val gradeLevel: Int,
    val streakDays: Int,
    val totalStudyHours: Float,
    val quizzesCompleted: Int,
    val averageScore: Int,
    val completedChapters: Int,
    val totalQuestionsSolved: Int
)

@Entity(tableName = "video_status")
data class VideoStatusEntity(
    @PrimaryKey val videoId: String,
    val isDownloaded: Boolean,
    val watchProgressPercent: Float
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val gradeLevel: Int,
    val subjectId: String,
    val deckTitle: String,
    val term: String,
    val definition: String,
    val hint: String = "",
    val masteryLevel: Int = 0, // 0: Learning, 1: Reviewing, 2: Mastered
    val lastReviewedTimestamp: Long = 0L,
    val isFavorite: Boolean = false
)
