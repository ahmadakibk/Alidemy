package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.Grade
import com.example.data.model.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlidemyTopBar(
    currentGrade: Grade,
    onGradeSelected: (Grade) -> Unit,
    syncStatus: String,
    onSyncClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gradeMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Brand logo & title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Reset to home or refresh */ }
                ) {
                    // Official Brand Logo
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_alidemy_logo_master),
                        contentDescription = "Alidemy Logo",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Alidemy",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    letterSpacing = (-0.5).sp
                                )
                            )
                        }
                        Text(
                            text = "Grades 6–12 Academy",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Grade Selector & Sync Status Actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Grade Selector Pill Dropdown
                    Box {
                        FilterChip(
                            selected = true,
                            onClick = { gradeMenuExpanded = true },
                            label = {
                                Text(
                                    text = currentGrade.shortName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Grade",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.testTag("grade_selector_button")
                        )

                        DropdownMenu(
                            expanded = gradeMenuExpanded,
                            onDismissRequest = { gradeMenuExpanded = false }
                        ) {
                            Text(
                                text = "Select Curriculum Grade",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Divider()
                            Grade.entries.forEach { grade ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = grade.displayName,
                                                fontWeight = if (grade == currentGrade) FontWeight.Bold else FontWeight.Normal
                                            )
                                            if (grade == currentGrade) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onGradeSelected(grade)
                                        gradeMenuExpanded = false
                                    },
                                    modifier = Modifier.testTag("grade_option_${grade.level}")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Sync Status Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier
                            .clickable { onSyncClick() }
                            .testTag("sync_status_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sync",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sync",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Profile / Analytics Quick Icon
                    IconButton(
                        onClick = onAnalyticsClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .testTag("top_bar_analytics_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile & Analytics",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectFilterRow(
    selectedSubject: Subject?,
    onSubjectSelected: (Subject?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedSubject == null,
            onClick = { onSubjectSelected(null) },
            label = { Text("All Subjects", fontSize = 12.sp) },
            modifier = Modifier.testTag("filter_all_subjects")
        )
        Subject.entries.take(4).forEach { subject ->
            FilterChip(
                selected = selectedSubject == subject,
                onClick = { onSubjectSelected(if (selectedSubject == subject) null else subject) },
                label = { Text(subject.shortCode, fontSize = 12.sp) },
                modifier = Modifier.testTag("filter_subject_${subject.id}")
            )
        }
    }
}
