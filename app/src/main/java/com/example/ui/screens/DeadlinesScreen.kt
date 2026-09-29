package com.example.ui.screens

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeadlineType
import com.example.data.model.ExamDeadline
import com.example.data.model.Grade
import com.example.data.model.Subject
import com.example.ui.components.SubjectBadge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinesScreen(
    grade: Grade,
    deadlines: List<ExamDeadline>,
    onToggleStatus: (ExamDeadline) -> Unit,
    onDeleteDeadline: (String) -> Unit,
    onAddDeadline: (String, Subject, Int, String, DeadlineType) -> Unit,
    onTriggerNotification: (ExamDeadline) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Android 13+ Notification Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            snackbarMessage = "Notification permission granted! Exam reminders active."
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(imageVector = Icons.Default.AddAlert, contentDescription = "Add") },
                text = { Text("Add Exam / Deadline") },
                modifier = Modifier.testTag("add_deadline_fab")
            )
        },
        snackbarHost = {
            if (snackbarMessage != null) {
                Snackbar(
                    action = {
                        TextButton(onClick = { snackbarMessage = null }) {
                            Text("OK", color = Color.White)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(snackbarMessage ?: "")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Upcoming Exams & Deadlines",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Never miss a board exam, assignment submission, or quiz milestone",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (deadlines.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.EventAvailable, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("All caught up! No upcoming deadlines.", style = MaterialTheme.typography.titleMedium)
                        Text("Tap + to schedule an exam reminder.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(deadlines, key = { it.id }) { item ->
                        DeadlineCard(
                            deadline = item,
                            onToggleStatus = { onToggleStatus(item) },
                            onDelete = { onDeleteDeadline(item.id) },
                            onTriggerNotification = {
                                onTriggerNotification(item)
                                snackbarMessage = "Sent reminder alert for '${item.title}'!"
                            }
                        )
                    }
                }
            }
        }
    }

    // Add Deadline Dialog
    if (showAddDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newSubject by remember { mutableStateOf(Subject.MATHEMATICS) }
        var newDays by remember { mutableStateOf("3") }
        var newType by remember { mutableStateOf(DeadlineType.EXAM) }
        var newNotes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Schedule Exam / Assignment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        placeholder = { Text("e.g. Science Board Exam Term 1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("deadline_title_input")
                    )

                    Text("Subject", style = MaterialTheme.typography.labelSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Subject.entries.take(4).forEach { subj ->
                            FilterChip(
                                selected = newSubject == subj,
                                onClick = { newSubject = subj },
                                label = { Text(subj.shortCode, fontSize = 10.sp) }
                            )
                        }
                    }

                    Text("Type", style = MaterialTheme.typography.labelSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DeadlineType.entries.forEach { type ->
                            FilterChip(
                                selected = newType == type,
                                onClick = { newType = type },
                                label = { Text(type.name.take(4), fontSize = 10.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newDays,
                        onValueChange = { newDays = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Due In (Days from now)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newNotes,
                        onValueChange = { newNotes = it },
                        label = { Text("Study Notes / Syllabus") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            val daysInt = newDays.toIntOrNull() ?: 3
                            onAddDeadline(newTitle, newSubject, daysInt, newNotes, newType)
                            showAddDialog = false
                            snackbarMessage = "Added '$newTitle' with exam reminder!"
                        }
                    },
                    modifier = Modifier.testTag("save_deadline_button")
                ) {
                    Text("Save Reminder")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DeadlineCard(
    deadline: ExamDeadline,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit,
    onTriggerNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    val diffMillis = deadline.dueTimestamp - now
    val diffDays = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

    val countdownText = when {
        deadline.isCompleted -> "Completed ✓"
        diffMillis < 0 -> "Overdue"
        diffDays == 0 -> "Due Today!"
        diffDays == 1 -> "Due Tomorrow"
        else -> "In $diffDays days"
    }

    val isUrgent = diffDays <= 2 && !deadline.isCompleted

    val formattedDate = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(Date(deadline.dueTimestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUrgent) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("deadline_card_${deadline.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SubjectBadge(subject = deadline.subject)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = deadline.type.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        deadline.isCompleted -> Color(0xFFDCFCE7)
                        isUrgent -> Color(0xFFFEE2E2)
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }
                ) {
                    Text(
                        text = countdownText,
                        color = when {
                            deadline.isCompleted -> Color(0xFF166534)
                            isUrgent -> Color(0xFF991B1B)
                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = deadline.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (deadline.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (deadline.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = deadline.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Completed Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleStatus() }
                ) {
                    Checkbox(
                        checked = deadline.isCompleted,
                        onCheckedChange = { onToggleStatus() }
                    )
                    Text(
                        text = if (deadline.isCompleted) "Done" else "Mark Done",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Send Notification Test Trigger
                    IconButton(
                        onClick = onTriggerNotification,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Test Notification Alert",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
