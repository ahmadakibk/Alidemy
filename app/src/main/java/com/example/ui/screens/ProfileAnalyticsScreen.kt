package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Grade
import com.example.data.model.Subject
import com.example.data.model.UserProfile
import com.example.ui.components.StatMetricCard
import com.example.ui.components.StreakPill

data class AchievementBadge(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val color: Color
)

@Composable
fun ProfileAnalyticsScreen(
    grade: Grade,
    profile: UserProfile,
    syncStatus: String,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badges = listOf(
        AchievementBadge("7-Day Streak", "Studied 7 days in a row without pause", Icons.Default.LocalFireDepartment, true, Color(0xFFEA580C)),
        AchievementBadge("Quiz Master", "Scored 100% in a chapter quiz", Icons.Default.EmojiEvents, true, Color(0xFFEAB308)),
        AchievementBadge("Math Prodigy", "Solved 50+ quadratic and calculus questions", Icons.Default.Calculate, true, Color(0xFF2563EB)),
        AchievementBadge("Active Peer", "Posted questions and answered in student forum", Icons.Default.Forum, true, Color(0xFF0D9488)),
        AchievementBadge("Science Scholar", "Completed Optics and Chemical Reactions modules", Icons.Default.Science, profile.completedChapters >= 10, Color(0xFF9333EA))
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // 1. Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("profile_header_card")
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = profile.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString(""),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${grade.displayName} • Alidemy Scholar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        StreakPill(streakDays = profile.streakDays)
                    }
                }
            }
        }

        // 2. Metrics Grid
        item {
            Text(
                text = "Academic Performance Overview",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    StatMetricCard(
                        title = "Study Time",
                        value = "${profile.totalStudyHours}h",
                        subtitle = "Total hours logged",
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Avg Quiz Score",
                        value = "${profile.averageScore}%",
                        subtitle = "Across ${profile.quizzesCompleted} quizzes",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    StatMetricCard(
                        title = "Questions Solved",
                        value = "${profile.totalQuestionsSolved}",
                        subtitle = "Practice problems",
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Chapters Read",
                        value = "${profile.completedChapters}",
                        subtitle = "Curriculum modules",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Subject Mastery Bars
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Subject Mastery Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val subjectMastery = listOf(
                        Triple(Subject.MATHEMATICS, 0.90f, "90% (Advanced)"),
                        Triple(Subject.SCIENCE, 0.82f, "82% (Proficient)"),
                        Triple(Subject.SOCIAL_STUDIES, 0.74f, "74% (Good)"),
                        Triple(Subject.ENGLISH, 0.88f, "88% (Proficient)"),
                        Triple(Subject.COMPUTER_SCIENCE, 0.95f, "95% (Mastery)")
                    )

                    subjectMastery.forEach { (subj, pct, label) ->
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = subj.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(subj.hexColor),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { pct },
                                color = Color(subj.hexColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }
        }

        // 4. Achievement Badges
        item {
            Text(
                text = "Milestones & Badges",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                badges.forEach { badge ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (badge.isUnlocked) 1.dp else 0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (badge.isUnlocked) badge.color.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = badge.icon,
                                    contentDescription = badge.title,
                                    tint = if (badge.isUnlocked) badge.color else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = badge.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = badge.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (badge.isUnlocked) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Unlocked",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Multi-Device Sync Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Multi-device sync",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Seamless Cross-Device Sync",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Status: $syncStatus\nYour bookmarks, quiz scores, offline notes, and custom exam reminders are automatically backed up and synced.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSyncClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sync All Data Now")
                    }
                }
            }
        }
    }
}
