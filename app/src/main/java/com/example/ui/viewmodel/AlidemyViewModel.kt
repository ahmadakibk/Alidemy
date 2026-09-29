package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AlidemyDatabase
import com.example.data.model.*
import com.example.data.repository.AlidemyRepository
import com.example.notification.AlidemyNotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination(val label: String, val iconName: String) {
    HOME("Home", "Home"),
    STUDY_MATERIALS("Study", "MenuBook"),
    FLASHCARDS("Flashcards", "Style"),
    QUIZZES("Quizzes", "Quiz"),
    VIDEO_LECTURES("Lectures", "VideoLibrary"),
    FORUM("Peer Forum", "Forum"),
    DEADLINES("Exams", "EventNote"),
    ANALYTICS("Analytics", "Analytics")
}

data class QuizSessionState(
    val quiz: Quiz? = null,
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOptionIndex
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val timeRemainingSeconds: Int = 600,
    val isTimerActive: Boolean = false,
    val showExplanation: Boolean = false
)

data class VideoPlayerState(
    val lecture: VideoLecture? = null,
    val currentPositionSeconds: Int = 0,
    val isPlaying: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val selectedSubTab: Int = 0 // 0: Key Timestamps, 1: Full Transcript, 2: Notes
)

class AlidemyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlidemyRepository

    init {
        val db = AlidemyDatabase.getDatabase(application)
        repository = AlidemyRepository(db.alidemyDao())
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    // --- Navigation State ---
    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _destinationBackStack = mutableListOf(AppDestination.HOME)

    fun navigateTo(destination: AppDestination) {
        if (_currentDestination.value != destination) {
            _destinationBackStack.add(destination)
            _currentDestination.value = destination
        }
    }

    fun handleBackPress(): Boolean {
        // Close sub-screen if reading chapter or taking quiz
        if (_activeReadingChapter.value != null) {
            _activeReadingChapter.value = null
            return true
        }
        if (_quizSession.value.quiz != null) {
            _quizSession.value = QuizSessionState()
            return true
        }
        if (_activeVideoState.value.lecture != null) {
            _activeVideoState.value = VideoPlayerState()
            return true
        }
        if (_activeForumPost.value != null) {
            _activeForumPost.value = null
            return true
        }
        if (_destinationBackStack.size > 1) {
            _destinationBackStack.removeAt(_destinationBackStack.lastIndex)
            _currentDestination.value = _destinationBackStack.last()
            return true
        }
        return false
    }

    // --- Grade & Subject Filter State ---
    private val _selectedGrade = MutableStateFlow(Grade.GRADE_10)
    val selectedGrade: StateFlow<Grade> = _selectedGrade.asStateFlow()

    private val _selectedSubjectFilter = MutableStateFlow<Subject?>(null)
    val selectedSubjectFilter: StateFlow<Subject?> = _selectedSubjectFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setGrade(grade: Grade) {
        _selectedGrade.value = grade
        viewModelScope.launch {
            repository.updateGrade(grade.level)
        }
    }

    fun setSubjectFilter(subject: Subject?) {
        _selectedSubjectFilter.value = subject
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Reactive Data Streams ---
    val userProfile: StateFlow<UserProfile> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val syncStatus: StateFlow<String> = repository.syncState

    val chapters: StateFlow<List<Chapter>> = combine(
        _selectedGrade,
        _selectedSubjectFilter,
        _searchQuery
    ) { grade, subject, query ->
        Triple(grade, subject, query)
    }.flatMapLatest { (grade, subject, query) ->
        repository.getChapters(grade.level, subject?.id).map { list ->
            if (query.isBlank()) list
            else list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.summary.contains(query, ignoreCase = true) ||
                        it.keyConcepts.any { c -> c.contains(query, ignoreCase = true) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizzes: StateFlow<List<Quiz>> = _selectedGrade.flatMapLatest { grade ->
        repository.getQuizzesByGrade(grade.level)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizAttempts = repository.getQuizAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val videoLectures: StateFlow<List<VideoLecture>> = _selectedGrade.flatMapLatest { grade ->
        repository.getVideosByGrade(grade.level)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val forumPosts: StateFlow<List<ForumPost>> = combine(
        _selectedGrade,
        _selectedSubjectFilter,
        _searchQuery
    ) { grade, subject, query ->
        Triple(grade, subject, query)
    }.flatMapLatest { (grade, subject, query) ->
        repository.getForumPosts(grade.level).map { list ->
            var filtered = list
            if (subject != null) {
                filtered = filtered.filter { it.subject == subject }
            }
            if (query.isNotBlank()) {
                filtered = filtered.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.content.contains(query, ignoreCase = true) ||
                            it.tags.any { t -> t.contains(query, ignoreCase = true) }
                }
            }
            filtered
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingDeadlines: StateFlow<List<ExamDeadline>> = _selectedGrade.flatMapLatest { grade ->
        repository.getDeadlines(grade.level)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Flashcards State ---
    private val _selectedDeck = MutableStateFlow<String?>(null)
    val selectedDeck: StateFlow<String?> = _selectedDeck.asStateFlow()

    private val _currentCardIndex = MutableStateFlow(0)
    val currentCardIndex: StateFlow<Int> = _currentCardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    val availableDecks: StateFlow<List<String>> = _selectedGrade.flatMapLatest { grade ->
        repository.getDecks(grade.level)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcards: StateFlow<List<Flashcard>> = combine(
        _selectedGrade,
        _selectedSubjectFilter,
        _selectedDeck
    ) { grade, subject, deck ->
        Triple(grade, subject, deck)
    }.flatMapLatest { (grade, subject, deck) ->
        repository.getFlashcards(grade.level, subject?.id, deck)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDeck(deck: String?) {
        _selectedDeck.value = deck
        _currentCardIndex.value = 0
        _isCardFlipped.value = false
    }

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextCard(total: Int) {
        if (total > 0 && _currentCardIndex.value < total - 1) {
            _currentCardIndex.value += 1
            _isCardFlipped.value = false
        }
    }

    fun prevCard() {
        if (_currentCardIndex.value > 0) {
            _currentCardIndex.value -= 1
            _isCardFlipped.value = false
        }
    }

    fun setFlashcardMastery(cardId: String, level: Int, total: Int) {
        viewModelScope.launch {
            repository.updateFlashcardMastery(cardId, level)
            if (total > 0 && _currentCardIndex.value < total - 1) {
                _currentCardIndex.value += 1
                _isCardFlipped.value = false
            }
        }
    }

    fun toggleFlashcardFavorite(card: Flashcard) {
        viewModelScope.launch {
            repository.toggleFlashcardFavorite(card.id, card.isFavorite)
        }
    }

    fun createCustomFlashcard(term: String, definition: String, hint: String, subject: Subject, deckTitle: String) {
        viewModelScope.launch {
            repository.createFlashcard(
                gradeLevel = _selectedGrade.value.level,
                subject = subject,
                deckTitle = deckTitle,
                term = term,
                definition = definition,
                hint = hint
            )
        }
    }

    fun deleteFlashcard(cardId: String) {
        viewModelScope.launch {
            repository.deleteFlashcard(cardId)
            if (_currentCardIndex.value > 0) {
                _currentCardIndex.value -= 1
            }
            _isCardFlipped.value = false
        }
    }

    // --- Active Chapter Reader ---
    private val _activeReadingChapter = MutableStateFlow<Chapter?>(null)
    val activeReadingChapter: StateFlow<Chapter?> = _activeReadingChapter.asStateFlow()

    fun openChapter(chapter: Chapter) {
        _activeReadingChapter.value = chapter
    }

    fun closeChapter() {
        _activeReadingChapter.value = null
    }

    fun toggleBookmark(chapter: Chapter) {
        viewModelScope.launch {
            repository.toggleBookmark(chapter.id, chapter.isBookmarked)
            _activeReadingChapter.value = _activeReadingChapter.value?.copy(isBookmarked = !chapter.isBookmarked)
        }
    }

    fun toggleChapterCompleted(chapter: Chapter) {
        viewModelScope.launch {
            repository.toggleCompleted(chapter.id, chapter.isCompleted)
            _activeReadingChapter.value = _activeReadingChapter.value?.copy(isCompleted = !chapter.isCompleted)
        }
    }

    fun toggleChapterOfflineSaved(chapter: Chapter) {
        viewModelScope.launch {
            repository.toggleOfflineSaved(chapter.id, chapter.isOfflineSaved)
            _activeReadingChapter.value = _activeReadingChapter.value?.copy(isOfflineSaved = !chapter.isOfflineSaved)
        }
    }

    // --- Quiz Session ---
    private val _quizSession = MutableStateFlow(QuizSessionState())
    val quizSession: StateFlow<QuizSessionState> = _quizSession.asStateFlow()
    private var quizTimerJob: Job? = null

    fun startQuiz(quiz: Quiz) {
        quizTimerJob?.cancel()
        _quizSession.value = QuizSessionState(
            quiz = quiz,
            currentQuestionIndex = 0,
            userAnswers = emptyMap(),
            isSubmitted = false,
            score = 0,
            timeRemainingSeconds = quiz.durationMinutes * 60,
            isTimerActive = true,
            showExplanation = false
        )
        startQuizTimer()
    }

    private fun startQuizTimer() {
        quizTimerJob = viewModelScope.launch {
            while (_quizSession.value.timeRemainingSeconds > 0 && !_quizSession.value.isSubmitted) {
                delay(1000)
                _quizSession.value = _quizSession.value.copy(
                    timeRemainingSeconds = _quizSession.value.timeRemainingSeconds - 1
                )
            }
            if (_quizSession.value.timeRemainingSeconds <= 0 && !_quizSession.value.isSubmitted) {
                submitQuiz()
            }
        }
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        if (_quizSession.value.isSubmitted) return
        val current = _quizSession.value.userAnswers.toMutableMap()
        current[questionIndex] = optionIndex
        _quizSession.value = _quizSession.value.copy(userAnswers = current)
    }

    fun nextQuizQuestion() {
        val total = _quizSession.value.quiz?.questions?.size ?: 0
        if (_quizSession.value.currentQuestionIndex < total - 1) {
            _quizSession.value = _quizSession.value.copy(
                currentQuestionIndex = _quizSession.value.currentQuestionIndex + 1
            )
        }
    }

    fun previousQuizQuestion() {
        if (_quizSession.value.currentQuestionIndex > 0) {
            _quizSession.value = _quizSession.value.copy(
                currentQuestionIndex = _quizSession.value.currentQuestionIndex - 1
            )
        }
    }

    fun submitQuiz() {
        val state = _quizSession.value
        val quiz = state.quiz ?: return
        var score = 0
        quiz.questions.forEachIndexed { index, question ->
            val chosen = state.userAnswers[index]
            if (chosen == question.correctIndex) {
                score++
            }
        }
        quizTimerJob?.cancel()
        _quizSession.value = state.copy(
            isSubmitted = true,
            score = score,
            isTimerActive = false,
            showExplanation = true
        )
        viewModelScope.launch {
            repository.recordQuizAttempt(
                quizId = quiz.id,
                title = quiz.title,
                subject = quiz.subject,
                gradeLevel = quiz.gradeLevel,
                score = score,
                total = quiz.questions.size
            )
        }
    }

    fun exitQuiz() {
        quizTimerJob?.cancel()
        _quizSession.value = QuizSessionState()
    }

    // --- Video Player Session ---
    private val _activeVideoState = MutableStateFlow(VideoPlayerState())
    val activeVideoState: StateFlow<VideoPlayerState> = _activeVideoState.asStateFlow()
    private var videoPlaybackJob: Job? = null

    fun openVideoLecture(lecture: VideoLecture) {
        videoPlaybackJob?.cancel()
        val startPos = (lecture.watchProgressPercent * lecture.durationSeconds).toInt()
        _activeVideoState.value = VideoPlayerState(
            lecture = lecture,
            currentPositionSeconds = startPos,
            isPlaying = true,
            playbackSpeed = 1.0f,
            selectedSubTab = 0
        )
        startVideoPlaybackLoop()
    }

    private fun startVideoPlaybackLoop() {
        videoPlaybackJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _activeVideoState.value
                val lecture = current.lecture ?: break
                if (current.isPlaying) {
                    val nextPos = (current.currentPositionSeconds + 1 * current.playbackSpeed).toInt()
                    if (nextPos >= lecture.durationSeconds) {
                        _activeVideoState.value = current.copy(
                            currentPositionSeconds = lecture.durationSeconds,
                            isPlaying = false
                        )
                        repository.updateVideoWatchProgress(lecture.id, 1.0f)
                        break
                    } else {
                        _activeVideoState.value = current.copy(currentPositionSeconds = nextPos)
                        val progress = nextPos.toFloat() / lecture.durationSeconds
                        repository.updateVideoWatchProgress(lecture.id, progress)
                    }
                }
            }
        }
    }

    fun toggleVideoPlayPause() {
        val current = _activeVideoState.value
        _activeVideoState.value = current.copy(isPlaying = !current.isPlaying)
    }

    fun seekVideoTo(seconds: Int) {
        val current = _activeVideoState.value
        val lecture = current.lecture ?: return
        val clamped = seconds.coerceIn(0, lecture.durationSeconds)
        _activeVideoState.value = current.copy(currentPositionSeconds = clamped)
        repository.updateVideoWatchProgress(lecture.id, clamped.toFloat() / lecture.durationSeconds)
    }

    fun setVideoPlaybackSpeed(speed: Float) {
        _activeVideoState.value = _activeVideoState.value.copy(playbackSpeed = speed)
    }

    fun setVideoSubTab(tabIndex: Int) {
        _activeVideoState.value = _activeVideoState.value.copy(selectedSubTab = tabIndex)
    }

    fun toggleVideoDownload(lecture: VideoLecture) {
        repository.toggleVideoDownload(lecture.id)
        if (_activeVideoState.value.lecture?.id == lecture.id) {
            _activeVideoState.value = _activeVideoState.value.copy(
                lecture = _activeVideoState.value.lecture?.copy(isDownloaded = !lecture.isDownloaded)
            )
        }
    }

    fun closeVideoPlayer() {
        videoPlaybackJob?.cancel()
        _activeVideoState.value = VideoPlayerState()
    }

    // --- Forum Actions ---
    private val _activeForumPost = MutableStateFlow<ForumPost?>(null)
    val activeForumPost: StateFlow<ForumPost?> = _activeForumPost.asStateFlow()

    val activePostReplies: StateFlow<List<ForumReply>> = _activeForumPost.flatMapLatest { post ->
        if (post == null) flowOf(emptyList())
        else repository.getRepliesForPost(post.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectForumPost(post: ForumPost) {
        _activeForumPost.value = post
    }

    fun closeForumPost() {
        _activeForumPost.value = null
    }

    fun togglePostUpvote(post: ForumPost) {
        viewModelScope.launch {
            repository.togglePostUpvote(post.id, post.upvotes, post.isUpvotedByMe)
            if (_activeForumPost.value?.id == post.id) {
                val delta = if (post.isUpvotedByMe) -1 else 1
                _activeForumPost.value = _activeForumPost.value?.copy(
                    upvotes = post.upvotes + delta,
                    isUpvotedByMe = !post.isUpvotedByMe
                )
            }
        }
    }

    fun createQuestion(title: String, content: String, subject: Subject, tags: List<String>) {
        viewModelScope.launch {
            repository.createForumPost(
                gradeLevel = _selectedGrade.value.level,
                subject = subject,
                authorName = userProfile.value.name,
                title = title,
                content = content,
                tags = tags
            )
        }
    }

    fun addReplyToActivePost(content: String) {
        val post = _activeForumPost.value ?: return
        viewModelScope.launch {
            repository.addReply(
                postId = post.id,
                authorName = userProfile.value.name,
                authorRole = "Grade ${_selectedGrade.value.level} Student",
                content = content
            )
        }
    }

    fun markPostSolved(post: ForumPost, isSolved: Boolean) {
        viewModelScope.launch {
            repository.markPostSolved(post.id, isSolved)
            if (_activeForumPost.value?.id == post.id) {
                _activeForumPost.value = _activeForumPost.value?.copy(isSolved = isSolved)
            }
        }
    }

    // --- Deadline Actions ---
    fun toggleDeadlineStatus(deadline: ExamDeadline) {
        viewModelScope.launch {
            repository.toggleDeadlineStatus(deadline.id, deadline.isCompleted)
        }
    }

    fun deleteDeadline(deadlineId: String) {
        viewModelScope.launch {
            repository.deleteDeadline(deadlineId)
        }
    }

    fun addDeadline(
        title: String,
        subject: Subject,
        dueDaysFromNow: Int,
        notes: String,
        type: DeadlineType
    ) {
        viewModelScope.launch {
            val dueTimestamp = System.currentTimeMillis() + dueDaysFromNow * 86400000L
            repository.addDeadline(
                gradeLevel = _selectedGrade.value.level,
                subject = subject,
                title = title,
                dueTimestamp = dueTimestamp,
                notes = notes,
                type = type
            )
        }
    }

    fun triggerExamNotification(deadline: ExamDeadline) {
        val app = getApplication<Application>()
        AlidemyNotificationHelper.sendDeadlineAlert(
            context = app,
            notificationId = deadline.id.hashCode(),
            title = deadline.title,
            message = "Reminder: ${deadline.title} is coming up soon! Review your notes and practice questions.",
            subjectName = deadline.subject.displayName
        )
    }

    // --- Sync Simulation ---
    fun syncNow() {
        viewModelScope.launch {
            repository.triggerSync()
        }
    }
}
