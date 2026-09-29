package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Grade
import com.example.data.model.VideoLecture
import com.example.ui.components.OfflineAvailableBadge
import com.example.ui.components.SubjectBadge
import com.example.ui.viewmodel.VideoPlayerState

@Composable
fun VideoLecturesScreen(
    grade: Grade,
    lectures: List<VideoLecture>,
    playerState: VideoPlayerState,
    onOpenLecture: (VideoLecture) -> Unit,
    onClosePlayer: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onSetSubTab: (Int) -> Unit,
    onToggleDownload: (VideoLecture) -> Unit,
    modifier: Modifier = Modifier
) {
    if (playerState.lecture != null) {
        BackHandler { onClosePlayer() }
        VideoPlayerView(
            state = playerState,
            onClose = onClosePlayer,
            onTogglePlayPause = onTogglePlayPause,
            onSeekTo = onSeekTo,
            onSetSpeed = onSetSpeed,
            onSetSubTab = onSetSubTab,
            onToggleDownload = { onToggleDownload(playerState.lecture) },
            modifier = modifier
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Curated Video Lectures",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "High-definition structured lessons taught by top academic faculty",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (lectures.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No video lectures for this grade yet.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            } else {
                items(lectures, key = { it.id }) { lecture ->
                    val durationMin = lecture.durationSeconds / 60
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLecture(lecture) }
                            .testTag("lecture_card_${lecture.id}")
                    ) {
                        Column {
                            // Video Thumbnail Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .background(Color(0xFF0F172A))
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.85f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play Lecture",
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                // Badges on Thumbnail
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    SubjectBadge(subject = lecture.subject)
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "$durationMin min",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Progress bar on bottom edge
                                if (lecture.watchProgressPercent > 0f) {
                                    LinearProgressIndicator(
                                        progress = { lecture.watchProgressPercent },
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = Color.White.copy(alpha = 0.2f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .align(Alignment.BottomCenter)
                                    )
                                }
                            }

                            // Details
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = lecture.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${lecture.instructorName} • ${lecture.instructorTitle}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = lecture.summary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (lecture.isDownloaded) {
                                        OfflineAvailableBadge()
                                    } else {
                                        Text(
                                            text = "Stream or Cache Offline",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    FilledTonalButton(
                                        onClick = { onOpenLecture(lecture) },
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text("Watch Lesson")
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

@Composable
fun VideoPlayerView(
    state: VideoPlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onSetSubTab: (Int) -> Unit,
    onToggleDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lecture = state.lecture ?: return
    val totalSeconds = lecture.durationSeconds
    val currentSec = state.currentPositionSeconds

    fun formatTime(sec: Int): String {
        val m = sec / 60
        val s = sec % 60
        return String.format(java.util.Locale.US, "%02d:%02d", m, s)
    }

    val subTabs = listOf("Timestamps", "Transcript", "Key Takeaways")

    Column(modifier = modifier.fillMaxSize()) {
        // Video Viewport Area (Simulated Media Player Canvas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
        ) {
            // Player Top Overlay
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.TopCenter)
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = lecture.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )

                IconButton(onClick = onToggleDownload) {
                    Icon(
                        imageVector = if (lecture.isDownloaded) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                        contentDescription = "Download",
                        tint = if (lecture.isDownloaded) Color(0xFF34D399) else Color.White
                    )
                }
            }

            // Player Center Controls (Rewind 10s, Play/Pause, Forward 10s)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                IconButton(onClick = { onSeekTo(currentSec - 10) }) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Back 10s",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable { onTogglePlayPause() }
                        .testTag("video_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = { onSeekTo(currentSec + 10) }) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10s",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Player Bottom Seek Bar & Timers
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Slider(
                    value = currentSec.toFloat(),
                    onValueChange = { onSeekTo(it.toInt()) },
                    valueRange = 0f..totalSeconds.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth().height(24.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${formatTime(currentSec)} / ${formatTime(totalSeconds)}",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )

                    // Playback Speed Selector Chips
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                            val isSelected = state.playbackSpeed == speed
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.clickable { onSetSpeed(speed) }
                            ) {
                                Text(
                                    text = "${speed}x",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sub Tabs
        TabRow(selectedTabIndex = state.selectedSubTab) {
            subTabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = state.selectedSubTab == index,
                    onClick = { onSetSubTab(index) },
                    text = { Text(tabTitle, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        // Sub Tab Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (state.selectedSubTab) {
                0 -> {
                    // Timestamps & Topics
                    Text(
                        text = "Jump to Chapter Topic",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    lecture.keyTimestamps.forEach { ts ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { onSeekTo(ts.seconds) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = formatTime(ts.seconds),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = ts.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Jump",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Full Transcript
                    Text(
                        text = "Audio Transcript",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lecture.transcript,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp)
                    )
                }
                2 -> {
                    // Key Takeaways & Summary
                    Text(
                        text = "Key Takeaways & Summary",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lecture.summary,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp)
                    )
                }
            }
        }
    }
}
