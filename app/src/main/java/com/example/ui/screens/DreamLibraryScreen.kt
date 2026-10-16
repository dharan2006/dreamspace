package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Dream
import com.example.ui.components.AppHeader
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicOnSurface
import com.example.ui.theme.CosmicOnSurfaceVariant
import com.example.ui.theme.CosmicPrimary
import com.example.ui.theme.CosmicPrimaryContainer
import com.example.ui.theme.CosmicSecondary
import com.example.ui.theme.CosmicSecondaryContainer
import com.example.ui.theme.CosmicSurfaceContainer
import com.example.ui.theme.CosmicSurfaceContainerHigh
import com.example.ui.theme.CosmicSurfaceContainerHighest
import com.example.ui.theme.CosmicSurfaceContainerLow
import com.example.ui.theme.CosmicSurfaceContainerLowest
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.CosmicTertiaryContainer
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.AppTab
import com.example.viewmodel.DreamViewModel
import com.example.viewmodel.LibraryFilter
import com.example.viewmodel.LibraryLayout

@Composable
fun DreamLibraryScreen(
    viewModel: DreamViewModel,
    onDreamClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredDreams by viewModel.filteredDreams.collectAsState()
    val allDreams by viewModel.dreams.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilter by viewModel.libraryFilter.collectAsState()
    val currentLayout by viewModel.libraryLayout.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .padding(bottom = 100.dp)
    ) {
        AppHeader(
            onProfileClick = { viewModel.selectTab(AppTab.PROFILE) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Title & View Layout Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Dream Library",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CosmicOnSurface
                    )
                }

                // Grid / List toggle
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerHigh.copy(alpha = 0.7f))
                        .padding(2.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.libraryLayout.value = LibraryLayout.GRID },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentLayout == LibraryLayout.GRID) CosmicPrimary.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Grid",
                            tint = if (currentLayout == LibraryLayout.GRID) CosmicPrimary else CosmicOnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.libraryLayout.value = LibraryLayout.LIST },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentLayout == LibraryLayout.LIST) CosmicPrimary.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewAgenda,
                            contentDescription = "List",
                            tint = if (currentLayout == LibraryLayout.LIST) CosmicPrimary else CosmicOnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Subtitle & Synaptic Sync indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CosmicTertiary)
                    )
                    Text(
                        text = "${allDreams.size} Dreams recorded across 4 categories",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                }
                Text(
                    text = "SYNAPTIC SYNC",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 1.sp,
                    color = CosmicSecondary
                )
            }

            // Search Bar & Filter trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = {
                        Text(
                            text = "Search dreams, symbols, emotions...",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            color = CosmicOnSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CosmicPrimary.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = CosmicOnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CosmicSurfaceContainerLowest.copy(alpha = 0.7f),
                        unfocusedContainerColor = CosmicSurfaceContainerLowest.copy(alpha = 0.7f),
                        focusedBorderColor = CosmicPrimary.copy(alpha = 0.4f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.06f),
                        focusedTextColor = CosmicOnSurface,
                        unfocusedTextColor = CosmicOnSurface
                    ),
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CosmicSurfaceContainer.copy(alpha = 0.8f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.showDreamMap.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = CosmicPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Category Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LibraryFilterChip(
                    title = "All",
                    icon = Icons.Default.Stars,
                    isSelected = currentFilter == LibraryFilter.ALL,
                    onClick = { viewModel.libraryFilter.value = LibraryFilter.ALL }
                )
                LibraryFilterChip(
                    title = "Favorites",
                    icon = Icons.Default.Favorite,
                    iconTint = CosmicSecondary,
                    isSelected = currentFilter == LibraryFilter.FAVORITES,
                    onClick = { viewModel.libraryFilter.value = LibraryFilter.FAVORITES }
                )
                LibraryFilterChip(
                    title = "Lucid Dreams",
                    icon = Icons.Default.WbTwilight,
                    iconTint = CosmicTertiary,
                    isSelected = currentFilter == LibraryFilter.LUCID,
                    onClick = { viewModel.libraryFilter.value = LibraryFilter.LUCID }
                )
                LibraryFilterChip(
                    title = "Archived",
                    icon = Icons.Default.Inventory2,
                    isSelected = currentFilter == LibraryFilter.ARCHIVED,
                    onClick = { viewModel.libraryFilter.value = LibraryFilter.ARCHIVED }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dreams Grid / List View
        if (filteredDreams.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🌌", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Dreams Found",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CosmicOnSurface
                    )
                    Text(
                        text = "Try searching for another symbol or reset your filter.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (currentLayout == LibraryLayout.GRID) 2 else 1),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredDreams, key = { it.id }) { dream ->
                    DreamCard(
                        dream = dream,
                        isGrid = currentLayout == LibraryLayout.GRID,
                        onClick = { onDreamClick(dream.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(dream.id) }
                    )
                }

                // Bottom Archetype Analysis Card item
                item {
                    ArchetypeAnalysisBanner(
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DreamCard(
    dream: Dream,
    isGrid: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CosmicSurfaceContainerLow.copy(alpha = 0.8f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        // Thumbnail Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (isGrid) 4f / 3f else 16f / 9f)
                .clip(RoundedCornerShape(14.dp))
                .background(CosmicSurfaceContainerLowest)
        ) {
            AsyncImage(
                model = dream.imageUrl,
                contentDescription = dream.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // Emotion pill indicator top-left
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerLowest.copy(alpha = 0.85f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(
                            when (dream.mood) {
                                "Wonder" -> CosmicSecondary
                                "Serene" -> CosmicTertiary
                                "Euphoric" -> CosmicSecondaryContainer
                                else -> CosmicPrimary
                            }
                        )
                )
                Text(
                    text = dream.mood,
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = CosmicOnSurface
                )
            }

            // Favorite Button top-right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHigh.copy(alpha = 0.8f))
                    .clickable { onFavoriteToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (dream.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (dream.isFavorite) CosmicSecondary else CosmicOnSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }

            // Date Badge bottom-left
            Text(
                text = dream.date,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                color = CosmicOnSurface,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainer.copy(alpha = 0.75f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title and Narrative Preview
        Text(
            text = dream.title,
            fontFamily = PlayfairFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = CosmicOnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = dream.narrative,
            fontFamily = PlusJakartaFontFamily,
            fontSize = 11.sp,
            color = CosmicOnSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )

        // Tag and Forward Arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CosmicTertiaryContainer.copy(alpha = 0.25f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Lucid ${dream.lucidityPercent}%",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 10.sp,
                    color = CosmicTertiary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View",
                tint = CosmicPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun LibraryFilterChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconTint: Color = CosmicPrimary
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isSelected) Brush.horizontalGradient(
                    listOf(CosmicPrimary, CosmicPrimaryContainer)
                )
                else Brush.linearGradient(
                    listOf(CosmicSurfaceContainer.copy(alpha = 0.7f), CosmicSurfaceContainer.copy(alpha = 0.7f))
                )
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else iconTint,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = title,
                fontFamily = PlusJakartaFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp,
                color = if (isSelected) Color.White else CosmicOnSurfaceVariant
            )
        }
    }
}

@Composable
fun ArchetypeAnalysisBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(CosmicSurfaceContainerLow, CosmicSurfaceContainerHigh)
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = CosmicTertiary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "ARCHETYPE ANALYSIS",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    color = CosmicTertiary
                )
            }
            Text(
                text = "Flight & Levitation",
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = CosmicOnSurface,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = "Present in 42% of this month's recorded lucid dreams.",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 11.sp,
                color = CosmicOnSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(CosmicPrimary, CosmicSecondary))
                )
                .padding(2.dp)
                .clip(CircleShape)
                .background(CosmicSurfaceContainerLowest),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FlightTakeoff,
                contentDescription = null,
                tint = CosmicPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
