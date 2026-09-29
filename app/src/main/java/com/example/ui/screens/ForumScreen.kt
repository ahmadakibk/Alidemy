package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ForumPost
import com.example.data.model.ForumReply
import com.example.data.model.Grade
import com.example.data.model.Subject
import com.example.ui.components.SubjectBadge
import com.example.ui.components.SubjectFilterRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    grade: Grade,
    posts: List<ForumPost>,
    selectedSubject: Subject?,
    onSubjectSelected: (Subject?) -> Unit,
    activePost: ForumPost?,
    activePostReplies: List<ForumReply>,
    onSelectPost: (ForumPost) -> Unit,
    onClosePost: () -> Unit,
    onToggleUpvote: (ForumPost) -> Unit,
    onCreateQuestion: (String, String, Subject, List<String>) -> Unit,
    onAddReply: (String) -> Unit,
    onMarkSolved: (ForumPost, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAskDialog by remember { mutableStateOf(false) }

    if (activePost != null) {
        BackHandler { onClosePost() }
        ForumThreadView(
            post = activePost,
            replies = activePostReplies,
            onBack = onClosePost,
            onToggleUpvote = { onToggleUpvote(activePost) },
            onAddReply = onAddReply,
            onToggleSolved = { onMarkSolved(activePost, !activePost.isSolved) },
            modifier = modifier
        )
    } else {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showAskDialog = true },
                    icon = { Icon(imageVector = Icons.Default.AddComment, contentDescription = "Ask") },
                    text = { Text("Ask Question") },
                    modifier = Modifier.testTag("ask_question_fab")
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Header & Subject Filter
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Peer-to-Peer Study Forum",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Ask homework doubts, solve academic challenges, and collaborate with peers",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SubjectFilterRow(
                        selectedSubject = selectedSubject,
                        onSubjectSelected = onSubjectSelected
                    )
                }

                if (posts.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No discussions found for this subject.", style = MaterialTheme.typography.titleMedium)
                            Text("Be the first student to ask a question!", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(posts, key = { it.id }) { post ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectPost(post) }
                                    .testTag("forum_post_card_${post.id}")
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            SubjectBadge(subject = post.subject)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = post.authorName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        if (post.isSolved) {
                                            Surface(
                                                color = Color(0xFFDCFCE7),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "✓ SOLVED",
                                                    color = Color(0xFF166534),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = post.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = post.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Tags
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        post.tags.forEach { tag ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant
                                            ) {
                                                Text(
                                                    text = "#$tag",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Actions
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        // Upvote Pill Button
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (post.isUpvotedByMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable { onToggleUpvote(post) }
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ThumbUp,
                                                    contentDescription = "Upvote",
                                                    tint = if (post.isUpvotedByMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${post.upvotes}",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = if (post.isUpvotedByMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.ChatBubbleOutline,
                                                contentDescription = "Replies",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${post.replyCount} answers",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Ask Question Modal Dialog
    if (showAskDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newContent by remember { mutableStateOf("") }
        var newSubject by remember { mutableStateOf(Subject.MATHEMATICS) }
        var newTags by remember { mutableStateOf("Algebra, Doubts") }

        AlertDialog(
            onDismissRequest = { showAskDialog = false },
            title = { Text("Ask a Study Question") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Question Title") },
                        placeholder = { Text("e.g. How to derive quadratic formula?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("ask_title_input")
                    )

                    // Subject dropdown or chips
                    Text("Subject", style = MaterialTheme.typography.labelMedium)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Subject.entries.take(4).forEach { subj ->
                            FilterChip(
                                selected = newSubject == subj,
                                onClick = { newSubject = subj },
                                label = { Text(subj.shortCode, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Detailed Explanation / Question") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth().testTag("ask_content_input")
                    )

                    OutlinedTextField(
                        value = newTags,
                        onValueChange = { newTags = it },
                        label = { Text("Tags (comma separated)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            val tagsList = newTags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            onCreateQuestion(newTitle, newContent, newSubject, tagsList)
                            showAskDialog = false
                        }
                    },
                    modifier = Modifier.testTag("submit_question_button")
                ) {
                    Text("Post Question")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ForumThreadView(
    post: ForumPost,
    replies: List<ForumReply>,
    onBack: () -> Unit,
    onToggleUpvote: () -> Unit,
    onAddReply: (String) -> Unit,
    onToggleSolved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var replyText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Discussion Thread",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    FilterChip(
                        selected = post.isSolved,
                        onClick = onToggleSolved,
                        label = { Text(if (post.isSolved) "Solved ✓" else "Mark Solved") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    )
                }
            }
        },
        bottomBar = {
            Surface(tonalElevation = 6.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Write your explanation or answer...") },
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("forum_reply_input"),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (replyText.isNotBlank()) {
                                onAddReply(replyText)
                                replyText = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .testTag("send_reply_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Reply",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Question Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SubjectBadge(subject = post.subject)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Asked by ${post.authorName} (${post.authorRole})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilledTonalButton(onClick = onToggleUpvote) {
                                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upvote (${post.upvotes})")
                            }

                            Text(
                                text = "${replies.size} Responses",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Answers Header
            item {
                Text(
                    text = "Peer & Mentor Answers",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (replies.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No answers yet. Share your solution below!")
                        }
                    }
                }
            } else {
                items(replies, key = { it.id }) { reply ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (reply.isAcceptedSolution) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                    ) {
                                        Text(
                                            text = reply.authorName.take(1),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = reply.authorName,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                        )
                                        Text(
                                            text = reply.authorRole,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                if (reply.isAcceptedSolution) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFDCFCE7)
                                    ) {
                                        Text(
                                            text = "ACCEPTED SOLUTION ✓",
                                            color = Color(0xFF166534),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = reply.content,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}
