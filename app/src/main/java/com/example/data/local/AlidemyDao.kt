package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlidemyDao {

    // --- Chapters ---
    @Query("SELECT * FROM chapters WHERE gradeLevel = :grade ORDER BY chapterNumber ASC")
    fun getChaptersByGrade(grade: Int): Flow<List<CachedChapterEntity>>

    @Query("SELECT * FROM chapters WHERE gradeLevel = :grade AND subjectId = :subjectId ORDER BY chapterNumber ASC")
    fun getChaptersByGradeAndSubject(grade: Int, subjectId: String): Flow<List<CachedChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :id LIMIT 1")
    fun getChapterById(id: String): Flow<CachedChapterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<CachedChapterEntity>)

    @Query("UPDATE chapters SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun updateBookmark(id: String, bookmarked: Boolean)

    @Query("UPDATE chapters SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompleted(id: String, completed: Boolean)

    @Query("UPDATE chapters SET isOfflineSaved = :saved WHERE id = :id")
    suspend fun updateOfflineSaved(id: String, saved: Boolean)

    // --- Quiz Attempts ---
    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts WHERE gradeLevel = :grade ORDER BY timestamp DESC")
    fun getQuizAttemptsByGrade(grade: Int): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttemptEntity)

    // --- Forum Posts ---
    @Query("SELECT * FROM forum_posts ORDER BY timestamp DESC")
    fun getAllForumPosts(): Flow<List<ForumPostEntity>>

    @Query("SELECT * FROM forum_posts WHERE gradeLevel = :grade ORDER BY timestamp DESC")
    fun getForumPostsByGrade(grade: Int): Flow<List<ForumPostEntity>>

    @Query("SELECT * FROM forum_posts WHERE id = :id LIMIT 1")
    fun getForumPostById(id: String): Flow<ForumPostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForumPosts(posts: List<ForumPostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForumPost(post: ForumPostEntity)

    @Query("UPDATE forum_posts SET upvotes = upvotes + :delta, isUpvotedByMe = :isUpvoted WHERE id = :id")
    suspend fun updatePostUpvotes(id: String, delta: Int, isUpvoted: Boolean)

    @Query("UPDATE forum_posts SET isSolved = :isSolved WHERE id = :id")
    suspend fun updatePostSolved(id: String, isSolved: Boolean)

    @Query("UPDATE forum_posts SET replyCount = replyCount + 1 WHERE id = :id")
    suspend fun incrementReplyCount(id: String)

    // --- Forum Replies ---
    @Query("SELECT * FROM forum_replies WHERE postId = :postId ORDER BY timestamp ASC")
    fun getRepliesForPost(postId: String): Flow<List<ForumReplyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReply(reply: ForumReplyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplies(replies: List<ForumReplyEntity>)

    // --- Exam Deadlines ---
    @Query("SELECT * FROM exam_deadlines WHERE gradeLevel = :grade ORDER BY dueTimestamp ASC")
    fun getDeadlinesByGrade(grade: Int): Flow<List<ExamDeadlineEntity>>

    @Query("SELECT * FROM exam_deadlines ORDER BY dueTimestamp ASC")
    fun getAllDeadlines(): Flow<List<ExamDeadlineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadlines(deadlines: List<ExamDeadlineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadline(deadline: ExamDeadlineEntity)

    @Query("UPDATE exam_deadlines SET isCompleted = :completed WHERE id = :id")
    suspend fun updateDeadlineStatus(id: String, completed: Boolean)

    @Query("DELETE FROM exam_deadlines WHERE id = :id")
    suspend fun deleteDeadline(id: String)

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // --- Video Status ---
    @Query("SELECT * FROM video_status")
    fun getAllVideoStatuses(): Flow<List<VideoStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoStatus(status: VideoStatusEntity)

    // --- Flashcards ---
    @Query("SELECT * FROM flashcards WHERE gradeLevel = :grade ORDER BY lastReviewedTimestamp ASC")
    fun getFlashcardsByGrade(grade: Int): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE gradeLevel = :grade AND subjectId = :subjectId ORDER BY lastReviewedTimestamp ASC")
    fun getFlashcardsByGradeAndSubject(grade: Int, subjectId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE gradeLevel = :grade AND deckTitle = :deckTitle ORDER BY lastReviewedTimestamp ASC")
    fun getFlashcardsByDeck(grade: Int, deckTitle: String): Flow<List<FlashcardEntity>>

    @Query("SELECT DISTINCT deckTitle FROM flashcards WHERE gradeLevel = :grade")
    fun getDecksForGrade(grade: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET masteryLevel = :masteryLevel, lastReviewedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateFlashcardMastery(id: String, masteryLevel: Int, timestamp: Long)

    @Query("UPDATE flashcards SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFlashcardFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcard(id: String)
}
