package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Flashcard
import com.example.data.model.Grade
import com.example.data.model.Subject
import com.example.ui.components.SubjectBadge
import com.example.ui.components.SubjectFilterRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    grade: Grade,
    flashcards: List<Flashcard>,
    availableDecks: List<String>,
    selectedDeck: String?,
    onSelectDeck: (String?) -> Unit,
    selectedSubject: Subject?,
    onSelectSubject: (Subject?) -> Unit,
    currentCardIndex: Int,
    isCardFlipped: Boolean,
    onFlipCard: () -> Unit,
    onNextCard: (Int) -> Unit,
    onPrevCard: () -> Unit,
    onSetMastery: (String, Int, Int) -> Unit,
    onToggleFavorite: (Flashcard) -> Unit,
    onCreateFlashcard: (String, String, String, Subject, String) -> Unit,
    onDeleteFlashcard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }

    val safeIndex = if (flashcards.isNotEmpty()) currentCardIndex.coerceIn(0, flashcards.size - 1) else 0
    val activeCard = flashcards.getOrNull(safeIndex)

    // Reset hint whenever card flips or advances
    LaunchedEffect(safeIndex, isCardFlipped) {
        showHint = false
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Card") },
                text = { Text("New Flashcard") },
                modifier = Modifier.testTag("add_flashcard_fab")
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Interactive Study Flashcards",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Test definitions & formulas with spaced repetition (Stored in Room)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Subject Filter
            item {
                SubjectFilterRow(
                    selectedSubject = selectedSubject,
                    onSubjectSelected = onSelectSubject
                )
            }

            // Decks Filter Row
            if (availableDecks.isNotEmpty()) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedDeck == null,
                                onClick = { onSelectDeck(null) },
                                label = { Text("All Decks (${flashcards.size})", fontSize = 12.sp) },
                                modifier = Modifier.testTag("deck_chip_all")
                            )
                        }
                        items(availableDecks) { deck ->
                            FilterChip(
                                selected = selectedDeck == deck,
                                onClick = { onSelectDeck(if (selectedDeck == deck) null else deck) },
                                label = { Text(deck, fontSize = 12.sp) },
                                modifier = Modifier.testTag("deck_chip_${deck.hashCode()}")
                            )
                        }
                    }
                }
            }

            if (flashcards.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Style, contentDescription = null, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No flashcards found in this deck.", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tap '+ New Flashcard' to create your own study terms!", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else if (activeCard != null) {
                // Interactive 3D Flippable Flashcard
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Card Counter & Deck Title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = activeCard.deckTitle,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Card ${safeIndex + 1} of ${flashcards.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Flip Animation
                        val rotation by animateFloatAsState(
                            targetValue = if (isCardFlipped) 180f else 0f,
                            animationSpec = tween(durationMillis = 400),
                            label = "CardFlipAnimation"
                        )

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCardFlipped) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 12f * density
                                }
                                .clickable { onFlipCard() }
                                .testTag("flippable_flashcard")
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                if (rotation <= 90f) {
                                    // FRONT OF CARD: Term / Concept
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            SubjectBadge(subject = activeCard.subject)

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(
                                                    onClick = { onToggleFavorite(activeCard) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = if (activeCard.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                                        contentDescription = "Favorite",
                                                        tint = if (activeCard.isFavorite) Color(0xFFEAB308) else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                                IconButton(
                                                    onClick = { onDeleteFlashcard(activeCard.id) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.DeleteOutline,
                                                        contentDescription = "Delete",
                                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "TERM / CONCEPT",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                letterSpacing = 1.sp
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = activeCard.term,
                                                style = MaterialTheme.typography.headlineMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center
                                                )
                                            )

                                            if (activeCard.hint.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(10.dp))
                                                if (showHint) {
                                                    Surface(
                                                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text(
                                                            text = "💡 Hint: ${activeCard.hint}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                } else {
                                                    TextButton(onClick = { showHint = true }) {
                                                        Text("Show Hint 💡", fontSize = 12.sp)
                                                    }
                                                }
                                            }
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.TouchApp,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Tap to Reveal Definition",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                } else {
                                    // BACK OF CARD: Definition / Explanation (Mirrored for proper reading)
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .graphicsLayer { rotationY = 180f },
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "DEFINITION & FORMULA",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }

                                            Surface(
                                                color = when (activeCard.masteryLevel) {
                                                    2 -> Color(0xFFDCFCE7)
                                                    1 -> Color(0xFFFEF3C7)
                                                    else -> MaterialTheme.colorScheme.surface
                                                },
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = when (activeCard.masteryLevel) {
                                                        2 -> "Mastered ✓"
                                                        1 -> "Reviewing"
                                                        else -> "Learning"
                                                    },
                                                    color = when (activeCard.masteryLevel) {
                                                        2 -> Color(0xFF166534)
                                                        1 -> Color(0xFF92400E)
                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                    },
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = activeCard.definition,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    lineHeight = 24.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FlipCameraAndroid,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Tap to Flip Back",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Mastery Actions Bar
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { onSetMastery(activeCard.id, 1, flashcards.size) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("mastery_review_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD97706))
                            ) {
                                Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Study Again")
                            }

                            Button(
                                onClick = { onSetMastery(activeCard.id, 2, flashcards.size) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("mastery_mastered_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Got It! ✓")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Card Navigation Bar (Prev / Next)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = onPrevCard,
                                enabled = safeIndex > 0
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Card")
                            }

                            LinearProgressIndicator(
                                progress = { (safeIndex + 1).toFloat() / flashcards.size },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )

                            IconButton(
                                onClick = { onNextCard(flashcards.size) },
                                enabled = safeIndex < flashcards.size - 1
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Card")
                            }
                        }
                    }
                }

                // Deck Summary / Quick Jump List
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Deck Terms Overview (${flashcards.size} Cards)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(flashcards) { card ->
                    val isCurrent = card.id == activeCard.id
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Jump directly to this card
                                val idx = flashcards.indexOfFirst { it.id == card.id }
                                if (idx != -1) {
                                    // advance or retreat
                                    while (safeIndex < idx) onNextCard(flashcards.size)
                                }
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = card.term,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = card.definition.take(60) + if (card.definition.length > 60) "..." else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (card.masteryLevel) {
                                    2 -> Color(0xFFDCFCE7)
                                    1 -> Color(0xFFFEF3C7)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Text(
                                    text = when (card.masteryLevel) {
                                        2 -> "Mastered"
                                        1 -> "Review"
                                        else -> "Learning"
                                    },
                                    color = when (card.masteryLevel) {
                                        2 -> Color(0xFF166534)
                                        1 -> Color(0xFF92400E)
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Create New Custom Flashcard Dialog
    if (showCreateDialog) {
        var termText by remember { mutableStateOf("") }
        var defText by remember { mutableStateOf("") }
        var hintText by remember { mutableStateOf("") }
        var cardSubject by remember { mutableStateOf(selectedSubject ?: Subject.MATHEMATICS) }
        var deckName by remember { mutableStateOf(selectedDeck ?: "Custom Study Terms") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Flashcard") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = termText,
                        onValueChange = { termText = it },
                        label = { Text("Term / Concept (Front)") },
                        placeholder = { Text("e.g. Ohm's Law, Mitochondria") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("flashcard_term_input")
                    )

                    OutlinedTextField(
                        value = defText,
                        onValueChange = { defText = it },
                        label = { Text("Definition / Formula (Back)") },
                        placeholder = { Text("e.g. V = I * R, Powerhouse of cell") },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("flashcard_definition_input")
                    )

                    OutlinedTextField(
                        value = hintText,
                        onValueChange = { hintText = it },
                        label = { Text("Optional Hint") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = deckName,
                        onValueChange = { deckName = it },
                        label = { Text("Deck Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Subject", style = MaterialTheme.typography.labelSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Subject.entries.take(4).forEach { subj ->
                            FilterChip(
                                selected = cardSubject == subj,
                                onClick = { cardSubject = subj },
                                label = { Text(subj.shortCode, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (termText.isNotBlank() && defText.isNotBlank()) {
                            onCreateFlashcard(termText, defText, hintText, cardSubject, deckName)
                            showCreateDialog = false
                        }
                    },
                    modifier = Modifier.testTag("save_flashcard_button")
                ) {
                    Text("Save to Room DB")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
