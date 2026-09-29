package com.example.data.model

enum class Grade(val level: Int, val displayName: String, val shortName: String) {
    GRADE_6(6, "Grade 6 (Middle School)", "Gr 6"),
    GRADE_7(7, "Grade 7 (Middle School)", "Gr 7"),
    GRADE_8(8, "Grade 8 (Middle School)", "Gr 8"),
    GRADE_9(9, "Grade 9 (Secondary)", "Gr 9"),
    GRADE_10(10, "Grade 10 (Secondary Boards)", "Gr 10"),
    GRADE_11(11, "Grade 11 (Senior Secondary)", "Gr 11"),
    GRADE_12(12, "Grade 12 (Board & Pre-College)", "Gr 12");

    companion object {
        fun fromLevel(level: Int): Grade = entries.find { it.level == level } ?: GRADE_10
    }
}

enum class Subject(val id: String, val displayName: String, val shortCode: String, val hexColor: Long) {
    MATHEMATICS("math", "Mathematics", "MATH", 0xFF1E40AF),
    SCIENCE("science", "Science (Physics, Chem, Bio)", "SCI", 0xFF0D9488),
    SOCIAL_STUDIES("social", "Social Studies (History & Geo)", "SST", 0xFFD97706),
    ENGLISH("english", "English Literature & Grammar", "ENG", 0xFF7C3AED),
    COMPUTER_SCIENCE("cs", "Computer Science & Coding", "CS", 0xFF0284C7);

    companion object {
        fun fromId(id: String): Subject = entries.find { it.id == id } ?: MATHEMATICS
    }
}

data class Chapter(
    val id: String,
    val gradeLevel: Int,
    val subject: Subject,
    val chapterNumber: Int,
    val title: String,
    val summary: String,
    val readTimeMinutes: Int,
    val fullContent: String,
    val keyConcepts: List<String>,
    val keyFormulas: List<String>,
    val isBookmarked: Boolean = false,
    val isOfflineSaved: Boolean = true,
    val isCompleted: Boolean = false
)

data class QuizQuestion(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class Quiz(
    val id: String,
    val title: String,
    val subject: Subject,
    val gradeLevel: Int,
    val durationMinutes: Int,
    val questions: List<QuizQuestion>
)

data class VideoTimestamp(
    val seconds: Int,
    val label: String
)

data class VideoLecture(
    val id: String,
    val gradeLevel: Int,
    val subject: Subject,
    val title: String,
    val instructorName: String,
    val instructorTitle: String,
    val durationSeconds: Int,
    val keyTimestamps: List<VideoTimestamp>,
    val summary: String,
    val transcript: String,
    val isDownloaded: Boolean = false,
    val watchProgressPercent: Float = 0f
)

data class ForumPost(
    val id: String,
    val gradeLevel: Int,
    val subject: Subject,
    val authorName: String,
    val authorRole: String,
    val title: String,
    val content: String,
    val upvotes: Int,
    val replyCount: Int,
    val isSolved: Boolean,
    val isUpvotedByMe: Boolean = false,
    val tags: List<String>,
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: String = "SYNCED" // "SYNCED" or "LOCAL_DRAFT"
)

data class ForumReply(
    val id: String,
    val postId: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val upvotes: Int = 0,
    val isAcceptedSolution: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class DeadlineType(val displayName: String) {
    EXAM("Term / Board Exam"),
    ASSIGNMENT("Homework / Assignment"),
    QUIZ_DEADLINE("Subject Quiz"),
    PROJECT("Lab / Project Work")
}

data class ExamDeadline(
    val id: String,
    val gradeLevel: Int,
    val subject: Subject,
    val title: String,
    val dueTimestamp: Long,
    val reminderHoursBefore: Int = 24,
    val notes: String = "",
    val isCompleted: Boolean = false,
    val type: DeadlineType = DeadlineType.EXAM
)

data class UserProfile(
    val name: String = "Alex Chen",
    val gradeLevel: Int = 10,
    val streakDays: Int = 7,
    val lastStudyTimestamp: Long = System.currentTimeMillis(),
    val totalStudyHours: Float = 42.5f,
    val quizzesCompleted: Int = 18,
    val averageScore: Int = 88,
    val completedChapters: Int = 24,
    val totalQuestionsSolved: Int = 156
)

data class Flashcard(
    val id: String,
    val gradeLevel: Int,
    val subject: Subject,
    val deckTitle: String,
    val term: String,
    val definition: String,
    val hint: String = "",
    val masteryLevel: Int = 0, // 0: Learning, 1: Reviewing, 2: Mastered
    val lastReviewedTimestamp: Long = 0L,
    val isFavorite: Boolean = false
)
