package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.viewmodel.AppDestination

fun getDestinationIcon(destination: AppDestination): ImageVector {
    return when (destination) {
        AppDestination.HOME -> Icons.Default.Home
        AppDestination.STUDY_MATERIALS -> Icons.Default.MenuBook
        AppDestination.FLASHCARDS -> Icons.Default.Style
        AppDestination.QUIZZES -> Icons.Default.Quiz
        AppDestination.VIDEO_LECTURES -> Icons.Default.VideoLibrary
        AppDestination.FORUM -> Icons.Default.Forum
        AppDestination.DEADLINES -> Icons.Default.EventNote
        AppDestination.ANALYTICS -> Icons.Default.Analytics
    }
}

@Composable
fun AlidemyBottomNavigationBar(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        tonalElevation = 6.dp
    ) {
        val navItems = listOf(
            AppDestination.HOME,
            AppDestination.STUDY_MATERIALS,
            AppDestination.FLASHCARDS,
            AppDestination.QUIZZES,
            AppDestination.VIDEO_LECTURES,
            AppDestination.FORUM
        )

        navItems.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = getDestinationIcon(destination),
                        contentDescription = destination.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = { Text(destination.label) },
                selected = isSelected,
                onClick = { onNavigate(destination) },
                modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
            )
        }
    }
}

@Composable
fun AlidemyNavigationRail(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Image(
                painter = painterResource(id = R.drawable.ic_alidemy_logo_master),
                contentDescription = "Alidemy Logo",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        modifier = modifier.testTag("navigation_rail")
    ) {
        val navItems = AppDestination.entries

        navItems.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = getDestinationIcon(destination),
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) },
                selected = isSelected,
                onClick = { onNavigate(destination) },
                modifier = Modifier.testTag("rail_item_${destination.name.lowercase()}")
            )
        }
    }
}
