package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AlidemyBottomNavigationBar
import com.example.ui.components.AlidemyNavigationRail
import com.example.ui.components.AlidemyTopBar
import com.example.ui.screens.*
import com.example.ui.theme.AlidemyTheme
import com.example.ui.viewmodel.AlidemyViewModel
import com.example.ui.viewmodel.AppDestination

class MainActivity : ComponentActivity() {

    private val viewModel: AlidemyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlidemyTheme {
                AlidemyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AlidemyApp(viewModel: AlidemyViewModel) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val selectedSubjectFilter by viewModel.selectedSubjectFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val syncStatus by viewModel.syncStatus.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()
    val quizAttempts by viewModel.quizAttempts.collectAsStateWithLifecycle()
    val videoLectures by viewModel.videoLectures.collectAsStateWithLifecycle()
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val upcomingDeadlines by viewModel.upcomingDeadlines.collectAsStateWithLifecycle()

    val activeReadingChapter by viewModel.activeReadingChapter.collectAsStateWithLifecycle()
    val quizSession by viewModel.quizSession.collectAsStateWithLifecycle()
    val activeVideoState by viewModel.activeVideoState.collectAsStateWithLifecycle()
    val activeForumPost by viewModel.activeForumPost.collectAsStateWithLifecycle()
    val activePostReplies by viewModel.activePostReplies.collectAsStateWithLifecycle()

    val flashcards by viewModel.flashcards.collectAsStateWithLifecycle()
    val availableDecks by viewModel.availableDecks.collectAsStateWithLifecycle()
    val selectedDeck by viewModel.selectedDeck.collectAsStateWithLifecycle()
    val currentCardIndex by viewModel.currentCardIndex.collectAsStateWithLifecycle()
    val isCardFlipped by viewModel.isCardFlipped.collectAsStateWithLifecycle()

    // Handle back button for sub-screens & navigation backstack
    BackHandler(enabled = true) {
        val handled = viewModel.handleBackPress()
        if (!handled && currentDestination != AppDestination.HOME) {
            viewModel.navigateTo(AppDestination.HOME)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        if (isWideScreen) {
            // Adaptive Desktop / Tablet Layout with Navigation Rail
            Row(modifier = Modifier.fillMaxSize()) {
                AlidemyNavigationRail(
                    currentDestination = currentDestination,
                    onNavigate = { viewModel.navigateTo(it) }
                )

                Column(modifier = Modifier.weight(1f)) {
                    AlidemyTopBar(
                        currentGrade = selectedGrade,
                        onGradeSelected = { viewModel.setGrade(it) },
                        syncStatus = syncStatus,
                        onSyncClick = { viewModel.syncNow() },
                        onAnalyticsClick = { viewModel.navigateTo(AppDestination.ANALYTICS) }
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        AppScreenContent(
                            destination = currentDestination,
                            viewModel = viewModel,
                            selectedGrade = selectedGrade,
                            selectedSubjectFilter = selectedSubjectFilter,
                            searchQuery = searchQuery,
                            userProfile = userProfile,
                            syncStatus = syncStatus,
                            chapters = chapters,
                            quizzes = quizzes,
                            quizAttempts = quizAttempts,
                            videoLectures = videoLectures,
                            forumPosts = forumPosts,
                            upcomingDeadlines = upcomingDeadlines,
                            activeReadingChapter = activeReadingChapter,
                            quizSession = quizSession,
                            activeVideoState = activeVideoState,
                            activeForumPost = activeForumPost,
                            activePostReplies = activePostReplies,
                            flashcards = flashcards,
                            availableDecks = availableDecks,
                            selectedDeck = selectedDeck,
                            currentCardIndex = currentCardIndex,
                            isCardFlipped = isCardFlipped
                        )
                    }
                }
            }
        } else {
            // Mobile Compact Layout with TopBar and BottomNavigationBar
            val isFullScreenSubView = activeReadingChapter != null ||
                    quizSession.quiz != null ||
                    activeVideoState.lecture != null ||
                    activeForumPost != null

            Scaffold(
                topBar = {
                    if (!isFullScreenSubView) {
                        AlidemyTopBar(
                            currentGrade = selectedGrade,
                            onGradeSelected = { viewModel.setGrade(it) },
                            syncStatus = syncStatus,
                            onSyncClick = { viewModel.syncNow() },
                            onAnalyticsClick = { viewModel.navigateTo(AppDestination.ANALYTICS) }
                        )
                    }
                },
                bottomBar = {
                    if (!isFullScreenSubView) {
                        AlidemyBottomNavigationBar(
                            currentDestination = currentDestination,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AppScreenContent(
                        destination = currentDestination,
                        viewModel = viewModel,
                        selectedGrade = selectedGrade,
                        selectedSubjectFilter = selectedSubjectFilter,
                        searchQuery = searchQuery,
                        userProfile = userProfile,
                        syncStatus = syncStatus,
                        chapters = chapters,
                        quizzes = quizzes,
                        quizAttempts = quizAttempts,
                        videoLectures = videoLectures,
                        forumPosts = forumPosts,
                        upcomingDeadlines = upcomingDeadlines,
                        activeReadingChapter = activeReadingChapter,
                        quizSession = quizSession,
                        activeVideoState = activeVideoState,
                        activeForumPost = activeForumPost,
                        activePostReplies = activePostReplies,
                        flashcards = flashcards,
                        availableDecks = availableDecks,
                        selectedDeck = selectedDeck,
                        currentCardIndex = currentCardIndex,
                        isCardFlipped = isCardFlipped
                    )
                }
            }
        }
    }
}

@Composable
fun AppScreenContent(
    destination: AppDestination,
    viewModel: AlidemyViewModel,
    selectedGrade: com.example.data.model.Grade,
    selectedSubjectFilter: com.example.data.model.Subject?,
    searchQuery: String,
    userProfile: com.example.data.model.UserProfile,
    syncStatus: String,
    chapters: List<com.example.data.model.Chapter>,
    quizzes: List<com.example.data.model.Quiz>,
    quizAttempts: List<com.example.data.local.QuizAttemptEntity>,
    videoLectures: List<com.example.data.model.VideoLecture>,
    forumPosts: List<com.example.data.model.ForumPost>,
    upcomingDeadlines: List<com.example.data.model.ExamDeadline>,
    activeReadingChapter: com.example.data.model.Chapter?,
    quizSession: com.example.ui.viewmodel.QuizSessionState,
    activeVideoState: com.example.ui.viewmodel.VideoPlayerState,
    activeForumPost: com.example.data.model.ForumPost?,
    activePostReplies: List<com.example.data.model.ForumReply>,
    flashcards: List<com.example.data.model.Flashcard>,
    availableDecks: List<String>,
    selectedDeck: String?,
    currentCardIndex: Int,
    isCardFlipped: Boolean
) {
    when (destination) {
        AppDestination.HOME -> HomeScreen(
            grade = selectedGrade,
            userProfile = userProfile,
            chapters = chapters,
            deadlines = upcomingDeadlines,
            quizzes = quizzes,
            forumPosts = forumPosts,
            syncStatus = syncStatus,
            onNavigate = { viewModel.navigateTo(it) },
            onOpenChapter = {
                viewModel.openChapter(it)
                viewModel.navigateTo(AppDestination.STUDY_MATERIALS)
            },
            onStartQuiz = {
                viewModel.startQuiz(it)
                viewModel.navigateTo(AppDestination.QUIZZES)
            },
            onOpenForumPost = {
                viewModel.selectForumPost(it)
                viewModel.navigateTo(AppDestination.FORUM)
            },
            onSyncClick = { viewModel.syncNow() }
        )

        AppDestination.STUDY_MATERIALS -> StudyMaterialScreen(
            grade = selectedGrade,
            chapters = chapters,
            selectedSubject = selectedSubjectFilter,
            onSubjectSelected = { viewModel.setSubjectFilter(it) },
            searchQuery = searchQuery,
            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
            activeChapter = activeReadingChapter,
            onOpenChapter = { viewModel.openChapter(it) },
            onCloseChapter = { viewModel.closeChapter() },
            onToggleBookmark = { viewModel.toggleBookmark(it) },
            onToggleCompleted = { viewModel.toggleChapterCompleted(it) },
            onToggleOfflineSaved = { viewModel.toggleChapterOfflineSaved(it) }
        )

        AppDestination.FLASHCARDS -> FlashcardScreen(
            grade = selectedGrade,
            flashcards = flashcards,
            availableDecks = availableDecks,
            selectedDeck = selectedDeck,
            onSelectDeck = { viewModel.selectDeck(it) },
            selectedSubject = selectedSubjectFilter,
            onSelectSubject = { viewModel.setSubjectFilter(it) },
            currentCardIndex = currentCardIndex,
            isCardFlipped = isCardFlipped,
            onFlipCard = { viewModel.flipCard() },
            onNextCard = { total -> viewModel.nextCard(total) },
            onPrevCard = { viewModel.prevCard() },
            onSetMastery = { id, level, total -> viewModel.setFlashcardMastery(id, level, total) },
            onToggleFavorite = { viewModel.toggleFlashcardFavorite(it) },
            onCreateFlashcard = { term, def, hint, subj, deck ->
                viewModel.createCustomFlashcard(term, def, hint, subj, deck)
            },
            onDeleteFlashcard = { viewModel.deleteFlashcard(it) }
        )

        AppDestination.QUIZZES -> QuizScreen(
            grade = selectedGrade,
            quizzes = quizzes,
            attempts = quizAttempts,
            quizSession = quizSession,
            onStartQuiz = { viewModel.startQuiz(it) },
            onSelectAnswer = { qIndex, optIndex -> viewModel.selectQuizAnswer(qIndex, optIndex) },
            onNextQuestion = { viewModel.nextQuizQuestion() },
            onPrevQuestion = { viewModel.previousQuizQuestion() },
            onSubmitQuiz = { viewModel.submitQuiz() },
            onExitQuiz = { viewModel.exitQuiz() }
        )

        AppDestination.VIDEO_LECTURES -> VideoLecturesScreen(
            grade = selectedGrade,
            lectures = videoLectures,
            playerState = activeVideoState,
            onOpenLecture = { viewModel.openVideoLecture(it) },
            onClosePlayer = { viewModel.closeVideoPlayer() },
            onTogglePlayPause = { viewModel.toggleVideoPlayPause() },
            onSeekTo = { viewModel.seekVideoTo(it) },
            onSetSpeed = { viewModel.setVideoPlaybackSpeed(it) },
            onSetSubTab = { viewModel.setVideoSubTab(it) },
            onToggleDownload = { viewModel.toggleVideoDownload(it) }
        )

        AppDestination.FORUM -> ForumScreen(
            grade = selectedGrade,
            posts = forumPosts,
            selectedSubject = selectedSubjectFilter,
            onSubjectSelected = { viewModel.setSubjectFilter(it) },
            activePost = activeForumPost,
            activePostReplies = activePostReplies,
            onSelectPost = { viewModel.selectForumPost(it) },
            onClosePost = { viewModel.closeForumPost() },
            onToggleUpvote = { viewModel.togglePostUpvote(it) },
            onCreateQuestion = { title, content, subject, tags ->
                viewModel.createQuestion(title, content, subject, tags)
            },
            onAddReply = { viewModel.addReplyToActivePost(it) },
            onMarkSolved = { post, isSolved -> viewModel.markPostSolved(post, isSolved) }
        )

        AppDestination.DEADLINES -> DeadlinesScreen(
            grade = selectedGrade,
            deadlines = upcomingDeadlines,
            onToggleStatus = { viewModel.toggleDeadlineStatus(it) },
            onDeleteDeadline = { viewModel.deleteDeadline(it) },
            onAddDeadline = { title, subject, days, notes, type ->
                viewModel.addDeadline(title, subject, days, notes, type)
            },
            onTriggerNotification = { viewModel.triggerExamNotification(it) }
        )

        AppDestination.ANALYTICS -> ProfileAnalyticsScreen(
            grade = selectedGrade,
            profile = userProfile,
            syncStatus = syncStatus,
            onSyncClick = { viewModel.syncNow() }
        )
    }
}
