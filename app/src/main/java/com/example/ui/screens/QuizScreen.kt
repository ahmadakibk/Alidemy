package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuizAttemptEntity
import com.example.data.model.Grade
import com.example.data.model.Quiz
import com.example.data.model.QuizQuestion
import com.example.ui.components.SubjectBadge
import com.example.ui.viewmodel.QuizSessionState

@Composable
fun QuizScreen(
    grade: Grade,
    quizzes: List<Quiz>,
    attempts: List<QuizAttemptEntity>,
    quizSession: QuizSessionState,
    onStartQuiz: (Quiz) -> Unit,
    onSelectAnswer: (Int, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onSubmitQuiz: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (quizSession.quiz != null) {
        BackHandler { onExitQuiz() }
        QuizRunnerView(
            session = quizSession,
            onSelectAnswer = onSelectAnswer,
            onNextQuestion = onNextQuestion,
            onPrevQuestion = onPrevQuestion,
            onSubmitQuiz = onSubmitQuiz,
            onExit = onExitQuiz,
            modifier = modifier
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Interactive Practice Quizzes",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Test your concepts with instant feedback & explanations",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (quizzes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No quizzes available for this grade yet.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            } else {
                items(quizzes, key = { it.id }) { quiz ->
                    val lastAttempt = attempts.firstOrNull { it.quizId == quiz.id }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_card_${quiz.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SubjectBadge(subject = quiz.subject)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "Duration",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${quiz.durationMinutes} mins",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = quiz.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${quiz.questions.size} Multiple Choice Questions • Passing: 60%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (lastAttempt != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                val pct = (lastAttempt.score.toFloat() / lastAttempt.totalQuestions * 100).toInt()
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (pct >= 60) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ) {
                                    Text(
                                        text = "Last Score: ${lastAttempt.score}/${lastAttempt.totalQuestions} ($pct%)",
                                        color = if (pct >= 60) Color(0xFF166534) else Color(0xFF991B1B),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onStartQuiz(quiz) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("start_quiz_btn_${quiz.id}")
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (lastAttempt != null) "Retake Quiz" else "Start Quiz")
                            }
                        }
                    }
                }
            }

            // Past Quiz Attempts Summary
            if (attempts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Recent Quiz Activity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(attempts.take(4)) { attempt ->
                    val pct = (attempt.score.toFloat() / attempt.totalQuestions * 100).toInt()
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = attempt.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Completed on ${java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(attempt.timestamp))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$pct%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (pct >= 60) Color(0xFF16A34A) else Color(0xFFDC2626)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizRunnerView(
    session: QuizSessionState,
    onSelectAnswer: (Int, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onSubmitQuiz: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quiz = session.quiz ?: return
    val currentIndex = session.currentQuestionIndex
    val currentQuestion = quiz.questions.getOrNull(currentIndex) ?: return
    val total = quiz.questions.size

    val mins = session.timeRemainingSeconds / 60
    val secs = session.timeRemainingSeconds % 60
    val timeFormatted = String.format(java.util.Locale.US, "%02d:%02d", mins, secs)

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    IconButton(onClick = onExit) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit Quiz")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = quiz.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = "Question ${currentIndex + 1} of $total",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Live Countdown Timer
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (session.timeRemainingSeconds < 60) Color(0xFFFEE2E2) else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = if (session.timeRemainingSeconds < 60) Color(0xFFDC2626) else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (session.timeRemainingSeconds < 60) Color(0xFFDC2626) else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(tonalElevation = 6.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onPrevQuestion,
                        enabled = currentIndex > 0
                    ) {
                        Text("Previous")
                    }

                    if (session.isSubmitted) {
                        Button(onClick = onExit) {
                            Text("Finish Review")
                        }
                    } else if (currentIndex == total - 1) {
                        Button(
                            onClick = onSubmitQuiz,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("submit_quiz_button")
                        ) {
                            Text("Submit Quiz")
                        }
                    } else {
                        Button(onClick = onNextQuestion) {
                            Text("Next Question")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / total },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Score Banner if submitted
            if (session.isSubmitted) {
                val pct = (session.score.toFloat() / total * 100).toInt()
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (pct >= 60) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (pct >= 60) "🎉 Quiz Passed!" else "Needs More Practice",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (pct >= 60) Color(0xFF166534) else Color(0xFF991B1B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Score: ${session.score} / $total ($pct%)",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (pct >= 60) Color(0xFF166534) else Color(0xFF991B1B)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Question Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Q${currentIndex + 1}. ${currentQuestion.questionText}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options list
            currentQuestion.options.forEachIndexed { optIndex, optionText ->
                val isSelected = session.userAnswers[currentIndex] == optIndex
                val isCorrect = optIndex == currentQuestion.correctIndex

                val borderColor = when {
                    session.isSubmitted && isCorrect -> Color(0xFF16A34A)
                    session.isSubmitted && isSelected && !isCorrect -> Color(0xFFDC2626)
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                }

                val bgColor = when {
                    session.isSubmitted && isCorrect -> Color(0xFFDCFCE7).copy(alpha = 0.7f)
                    session.isSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2).copy(alpha = 0.7f)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                        .clickable(enabled = !session.isSubmitted) {
                            onSelectAnswer(currentIndex, optIndex)
                        }
                        .testTag("quiz_option_${optIndex}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = ('A'.code + optIndex).toChar().toString(),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        if (session.isSubmitted && isCorrect) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = Color(0xFF16A34A)
                            )
                        } else if (session.isSubmitted && isSelected && !isCorrect) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Incorrect",
                                tint = Color(0xFFDC2626)
                            )
                        }
                    }
                }
            }

            // Explanation Section (shown if submitted or toggle is on)
            if (session.isSubmitted && session.showExplanation) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Explanation",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Explanation & Solution",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQuestion.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
