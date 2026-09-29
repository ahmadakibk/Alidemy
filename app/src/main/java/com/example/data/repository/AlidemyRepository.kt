package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import com.example.data.seed.SeedData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class AlidemyRepository(
    private val dao: AlidemyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val _inMemoryQuizzes = MutableStateFlow(SeedData.getInitialQuizzes())
    val allQuizzes: StateFlow<List<Quiz>> = _inMemoryQuizzes.asStateFlow()

    private val _inMemoryVideos = MutableStateFlow(SeedData.getInitialVideoLectures())
    val allVideos: StateFlow<List<VideoLecture>> = _inMemoryVideos.asStateFlow()

    private val _syncState = MutableStateFlow("Synced (Just now)")
    val syncState: StateFlow<String> = _syncState.asStateFlow()

    suspend fun initializeDatabaseIfEmpty() = withContext(ioDispatcher) {
        val existingProfile = dao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            // Seed chapters
            val seedChapters = SeedData.getInitialChapters().map { ch ->
                CachedChapterEntity(
                    id = ch.id,
                    gradeLevel = ch.gradeLevel,
                    subjectId = ch.subject.id,
                    chapterNumber = ch.chapterNumber,
                    title = ch.title,
                    summary = ch.summary,
                    readTimeMinutes = ch.readTimeMinutes,
                    fullContent = ch.fullContent,
                    keyConceptsRaw = ch.keyConcepts.joinToString("\n"),
                    keyFormulasRaw = ch.keyFormulas.joinToString("\n"),
                    isBookmarked = ch.isBookmarked,
                    isOfflineSaved = ch.isOfflineSaved,
                    isCompleted = ch.isCompleted
                )
            }
            dao.insertChapters(seedChapters)

            // Seed forum posts
            val seedPosts = SeedData.getInitialForumPosts().map { p ->
                ForumPostEntity(
                    id = p.id,
                    gradeLevel = p.gradeLevel,
                    subjectId = p.subject.id,
                    authorName = p.authorName,
                    authorRole = p.authorRole,
                    title = p.title,
                    content = p.content,
                    upvotes = p.upvotes,
                    replyCount = p.replyCount,
                    isSolved = p.isSolved,
                    isUpvotedByMe = p.isUpvotedByMe,
                    tagsRaw = p.tags.joinToString(","),
                    timestamp = p.timestamp,
                    syncStatus = p.syncStatus
                )
            }
            dao.insertForumPosts(seedPosts)

            // Seed forum replies
            val seedReplies = SeedData.getInitialForumReplies().map { r ->
                ForumReplyEntity(
                    id = r.id,
                    postId = r.postId,
                    authorName = r.authorName,
                    authorRole = r.authorRole,
                    content = r.content,
                    upvotes = r.upvotes,
                    isAcceptedSolution = r.isAcceptedSolution,
                    timestamp = r.timestamp
                )
            }
            dao.insertReplies(seedReplies)

            // Seed exam deadlines
            val seedDeadlines = SeedData.getInitialDeadlines().map { d ->
                ExamDeadlineEntity(
                    id = d.id,
                    gradeLevel = d.gradeLevel,
                    subjectId = d.subject.id,
                    title = d.title,
                    dueTimestamp = d.dueTimestamp,
                    reminderHoursBefore = d.reminderHoursBefore,
                    notes = d.notes,
                    isCompleted = d.isCompleted,
                    typeName = d.type.name
                )
            }
            dao.insertDeadlines(seedDeadlines)

            // Seed user profile
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Alex Chen",
                    gradeLevel = 10,
                    streakDays = 7,
                    totalStudyHours = 42.5f,
                    quizzesCompleted = 18,
                    averageScore = 88,
                    completedChapters = 24,
                    totalQuestionsSolved = 156
                )
            )

            // Seed some initial quiz attempts
            dao.insertQuizAttempt(
                QuizAttemptEntity(
                    quizId = "quiz_gr10_math_quad",
                    title = "Quadratic Equations Mastery Test",
                    subjectId = Subject.MATHEMATICS.id,
                    gradeLevel = 10,
                    score = 4,
                    totalQuestions = 4,
                    timestamp = System.currentTimeMillis() - 86400000L
                )
            )
            dao.insertQuizAttempt(
                QuizAttemptEntity(
                    quizId = "quiz_gr10_sci_light",
                    title = "Light: Reflection & Refraction Quiz",
                    subjectId = Subject.SCIENCE.id,
                    gradeLevel = 10,
                    score = 2,
                    totalQuestions = 3,
                    timestamp = System.currentTimeMillis() - 2 * 86400000L
                )
            )

            // Seed initial flashcards
            val seedFlashcards = SeedData.getInitialFlashcards().map { fc ->
                FlashcardEntity(
                    id = fc.id,
                    gradeLevel = fc.gradeLevel,
                    subjectId = fc.subject.id,
                    deckTitle = fc.deckTitle,
                    term = fc.term,
                    definition = fc.definition,
                    hint = fc.hint,
                    masteryLevel = fc.masteryLevel,
                    lastReviewedTimestamp = fc.lastReviewedTimestamp,
                    isFavorite = fc.isFavorite
                )
            }
            dao.insertFlashcards(seedFlashcards)
        }
    }

    // --- Chapters ---
    fun getChapters(grade: Int, subjectId: String? = null): Flow<List<Chapter>> {
        val flow = if (subjectId == null) {
            dao.getChaptersByGrade(grade)
        } else {
            dao.getChaptersByGradeAndSubject(grade, subjectId)
        }
        return flow.map { list ->
            list.map { it.toModel() }
        }
    }

    fun getChapter(id: String): Flow<Chapter?> {
        return dao.getChapterById(id).map { it?.toModel() }
    }

    suspend fun toggleBookmark(id: String, isBookmarked: Boolean) = withContext(ioDispatcher) {
        dao.updateBookmark(id, !isBookmarked)
    }

    suspend fun toggleCompleted(id: String, isCompleted: Boolean) = withContext(ioDispatcher) {
        dao.updateCompleted(id, !isCompleted)
    }

    suspend fun toggleOfflineSaved(id: String, isOfflineSaved: Boolean) = withContext(ioDispatcher) {
        dao.updateOfflineSaved(id, !isOfflineSaved)
    }

    // --- Quizzes ---
    fun getQuizzesByGrade(grade: Int): Flow<List<Quiz>> {
        return allQuizzes.map { list ->
            list.filter { it.gradeLevel == grade }
        }
    }

    fun getQuizAttempts(): Flow<List<QuizAttemptEntity>> {
        return dao.getAllQuizAttempts()
    }

    suspend fun recordQuizAttempt(
        quizId: String,
        title: String,
        subject: Subject,
        gradeLevel: Int,
        score: Int,
        total: Int
    ) = withContext(ioDispatcher) {
        dao.insertQuizAttempt(
            QuizAttemptEntity(
                quizId = quizId,
                title = title,
                subjectId = subject.id,
                gradeLevel = gradeLevel,
                score = score,
                totalQuestions = total,
                timestamp = System.currentTimeMillis()
            )
        )
        // Update user stats
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity(
            id = 1, name = "Alex Chen", gradeLevel = gradeLevel,
            streakDays = 7, totalStudyHours = 42.5f,
            quizzesCompleted = 18, averageScore = 88,
            completedChapters = 24, totalQuestionsSolved = 156
        )
        val newQuizzesCount = profile.quizzesCompleted + 1
        val newSolved = profile.totalQuestionsSolved + total
        val currentTotalScoreSum = (profile.averageScore * profile.quizzesCompleted)
        val newScorePercent = ((score.toFloat() / total) * 100).toInt()
        val newAvg = (currentTotalScoreSum + newScorePercent) / newQuizzesCount

        dao.insertOrUpdateProfile(
            profile.copy(
                quizzesCompleted = newQuizzesCount,
                totalQuestionsSolved = newSolved,
                averageScore = newAvg
            )
        )
    }

    // --- Forum Posts ---
    fun getForumPosts(grade: Int? = null): Flow<List<ForumPost>> {
        val flow = if (grade == null) dao.getAllForumPosts() else dao.getForumPostsByGrade(grade)
        return flow.map { list ->
            list.map { it.toModel() }
        }
    }

    fun getRepliesForPost(postId: String): Flow<List<ForumReply>> {
        return dao.getRepliesForPost(postId).map { list ->
            list.map { it.toModel() }
        }
    }

    suspend fun createForumPost(
        gradeLevel: Int,
        subject: Subject,
        authorName: String,
        title: String,
        content: String,
        tags: List<String>
    ) = withContext(ioDispatcher) {
        val postId = "post_${System.currentTimeMillis()}"
        val entity = ForumPostEntity(
            id = postId,
            gradeLevel = gradeLevel,
            subjectId = subject.id,
            authorName = authorName,
            authorRole = "Grade $gradeLevel Student",
            title = title,
            content = content,
            upvotes = 1,
            replyCount = 0,
            isSolved = false,
            isUpvotedByMe = true,
            tagsRaw = tags.joinToString(","),
            timestamp = System.currentTimeMillis(),
            syncStatus = "SYNCED"
        )
        dao.insertForumPost(entity)
    }

    suspend fun togglePostUpvote(postId: String, currentUpvotes: Int, currentlyUpvoted: Boolean) = withContext(ioDispatcher) {
        val delta = if (currentlyUpvoted) -1 else 1
        dao.updatePostUpvotes(postId, delta, !currentlyUpvoted)
    }

    suspend fun addReply(
        postId: String,
        authorName: String,
        authorRole: String,
        content: String
    ) = withContext(ioDispatcher) {
        val replyId = "reply_${System.currentTimeMillis()}"
        val entity = ForumReplyEntity(
            id = replyId,
            postId = postId,
            authorName = authorName,
            authorRole = authorRole,
            content = content,
            upvotes = 0,
            isAcceptedSolution = false,
            timestamp = System.currentTimeMillis()
        )
        dao.insertReply(entity)
        dao.incrementReplyCount(postId)
    }

    suspend fun markPostSolved(postId: String, isSolved: Boolean) = withContext(ioDispatcher) {
        dao.updatePostSolved(postId, isSolved)
    }

    // --- Deadlines ---
    fun getDeadlines(grade: Int? = null): Flow<List<ExamDeadline>> {
        val flow = if (grade == null) dao.getAllDeadlines() else dao.getDeadlinesByGrade(grade)
        return flow.map { list ->
            list.map { it.toModel() }
        }
    }

    suspend fun addDeadline(
        gradeLevel: Int,
        subject: Subject,
        title: String,
        dueTimestamp: Long,
        notes: String,
        type: DeadlineType
    ) = withContext(ioDispatcher) {
        val id = "dl_${System.currentTimeMillis()}"
        val entity = ExamDeadlineEntity(
            id = id,
            gradeLevel = gradeLevel,
            subjectId = subject.id,
            title = title,
            dueTimestamp = dueTimestamp,
            reminderHoursBefore = 24,
            notes = notes,
            isCompleted = false,
            typeName = type.name
        )
        dao.insertDeadline(entity)
    }

    suspend fun toggleDeadlineStatus(id: String, isCompleted: Boolean) = withContext(ioDispatcher) {
        dao.updateDeadlineStatus(id, !isCompleted)
    }

    suspend fun deleteDeadline(id: String) = withContext(ioDispatcher) {
        dao.deleteDeadline(id)
    }

    // --- Profile & Progress ---
    fun getUserProfile(): Flow<UserProfile> {
        return dao.getUserProfile().map { entity ->
            if (entity != null) {
                UserProfile(
                    name = entity.name,
                    gradeLevel = entity.gradeLevel,
                    streakDays = entity.streakDays,
                    totalStudyHours = entity.totalStudyHours,
                    quizzesCompleted = entity.quizzesCompleted,
                    averageScore = entity.averageScore,
                    completedChapters = entity.completedChapters,
                    totalQuestionsSolved = entity.totalQuestionsSolved
                )
            } else {
                UserProfile()
            }
        }
    }

    suspend fun updateGrade(newGrade: Int) = withContext(ioDispatcher) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfileEntity(
            id = 1, name = "Alex Chen", gradeLevel = 10,
            streakDays = 7, totalStudyHours = 42.5f,
            quizzesCompleted = 18, averageScore = 88,
            completedChapters = 24, totalQuestionsSolved = 156
        )
        dao.insertOrUpdateProfile(current.copy(gradeLevel = newGrade))
    }

    // --- Videos ---
    fun getVideosByGrade(grade: Int): Flow<List<VideoLecture>> {
        return allVideos.map { list ->
            list.filter { it.gradeLevel == grade }
        }
    }

    fun toggleVideoDownload(videoId: String) {
        val currentList = _inMemoryVideos.value
        _inMemoryVideos.value = currentList.map { vid ->
            if (vid.id == videoId) vid.copy(isDownloaded = !vid.isDownloaded) else vid
        }
    }

    fun updateVideoWatchProgress(videoId: String, progress: Float) {
        val currentList = _inMemoryVideos.value
        _inMemoryVideos.value = currentList.map { vid ->
            if (vid.id == videoId) vid.copy(watchProgressPercent = progress) else vid
        }
    }

    // --- Flashcards ---
    fun getFlashcards(grade: Int, subjectId: String? = null, deckTitle: String? = null): Flow<List<Flashcard>> {
        val flow = when {
            deckTitle != null -> dao.getFlashcardsByDeck(grade, deckTitle)
            subjectId != null -> dao.getFlashcardsByGradeAndSubject(grade, subjectId)
            else -> dao.getFlashcardsByGrade(grade)
        }
        return flow.map { list -> list.map { it.toModel() } }
    }

    fun getDecks(grade: Int): Flow<List<String>> {
        return dao.getDecksForGrade(grade)
    }

    suspend fun createFlashcard(
        gradeLevel: Int,
        subject: Subject,
        deckTitle: String,
        term: String,
        definition: String,
        hint: String
    ) = withContext(ioDispatcher) {
        val id = "fc_${System.currentTimeMillis()}"
        val entity = FlashcardEntity(
            id = id,
            gradeLevel = gradeLevel,
            subjectId = subject.id,
            deckTitle = deckTitle.ifBlank { "${subject.displayName} Terms" },
            term = term,
            definition = definition,
            hint = hint,
            masteryLevel = 0,
            lastReviewedTimestamp = System.currentTimeMillis()
        )
        dao.insertFlashcard(entity)
    }

    suspend fun updateFlashcardMastery(id: String, masteryLevel: Int) = withContext(ioDispatcher) {
        dao.updateFlashcardMastery(id, masteryLevel, System.currentTimeMillis())
    }

    suspend fun toggleFlashcardFavorite(id: String, isFavorite: Boolean) = withContext(ioDispatcher) {
        dao.updateFlashcardFavorite(id, !isFavorite)
    }

    suspend fun deleteFlashcard(id: String) = withContext(ioDispatcher) {
        dao.deleteFlashcard(id)
    }

    // --- Cloud Sync Simulation ---
    suspend fun triggerSync() = withContext(ioDispatcher) {
        _syncState.value = "Syncing across devices..."
        kotlinx.coroutines.delay(1200)
        _syncState.value = "🟢 Synced (All devices up to date)"
    }
}

// Mapper extension functions
private fun CachedChapterEntity.toModel(): Chapter {
    return Chapter(
        id = id,
        gradeLevel = gradeLevel,
        subject = Subject.fromId(subjectId),
        chapterNumber = chapterNumber,
        title = title,
        summary = summary,
        readTimeMinutes = readTimeMinutes,
        fullContent = fullContent,
        keyConcepts = if (keyConceptsRaw.isBlank()) emptyList() else keyConceptsRaw.split("\n"),
        keyFormulas = if (keyFormulasRaw.isBlank()) emptyList() else keyFormulasRaw.split("\n"),
        isBookmarked = isBookmarked,
        isOfflineSaved = isOfflineSaved,
        isCompleted = isCompleted
    )
}

private fun ForumPostEntity.toModel(): ForumPost {
    return ForumPost(
        id = id,
        gradeLevel = gradeLevel,
        subject = Subject.fromId(subjectId),
        authorName = authorName,
        authorRole = authorRole,
        title = title,
        content = content,
        upvotes = upvotes,
        replyCount = replyCount,
        isSolved = isSolved,
        isUpvotedByMe = isUpvotedByMe,
        tags = if (tagsRaw.isBlank()) emptyList() else tagsRaw.split(",").map { it.trim() },
        timestamp = timestamp,
        syncStatus = syncStatus
    )
}

private fun ForumReplyEntity.toModel(): ForumReply {
    return ForumReply(
        id = id,
        postId = postId,
        authorName = authorName,
        authorRole = authorRole,
        content = content,
        upvotes = upvotes,
        isAcceptedSolution = isAcceptedSolution,
        timestamp = timestamp
    )
}

private fun ExamDeadlineEntity.toModel(): ExamDeadline {
    val deadlineType = try {
        DeadlineType.valueOf(typeName)
    } catch (_: Exception) {
        DeadlineType.EXAM
    }
    return ExamDeadline(
        id = id,
        gradeLevel = gradeLevel,
        subject = Subject.fromId(subjectId),
        title = title,
        dueTimestamp = dueTimestamp,
        reminderHoursBefore = reminderHoursBefore,
        notes = notes,
        isCompleted = isCompleted,
        type = deadlineType
    )
}

private fun FlashcardEntity.toModel(): Flashcard {
    return Flashcard(
        id = id,
        gradeLevel = gradeLevel,
        subject = Subject.fromId(subjectId),
        deckTitle = deckTitle,
        term = term,
        definition = definition,
        hint = hint,
        masteryLevel = masteryLevel,
        lastReviewedTimestamp = lastReviewedTimestamp,
        isFavorite = isFavorite
    )
}
