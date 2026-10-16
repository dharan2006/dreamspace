package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicError
import com.example.ui.theme.CosmicOnSurface
import com.example.ui.theme.CosmicOnSurfaceVariant
import com.example.ui.theme.CosmicPrimary
import com.example.ui.theme.CosmicPrimaryContainer
import com.example.ui.theme.CosmicSecondary
import com.example.ui.theme.CosmicSecondaryContainer
import com.example.ui.theme.CosmicSurfaceBright
import com.example.ui.theme.CosmicSurfaceContainer
import com.example.ui.theme.CosmicSurfaceContainerHigh
import com.example.ui.theme.CosmicSurfaceContainerHighest
import com.example.ui.theme.CosmicSurfaceContainerLow
import com.example.ui.theme.CosmicSurfaceContainerLowest
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.CosmicTertiaryContainer
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.DetailTab
import com.example.viewmodel.DreamViewModel

@Composable
fun DreamDetailScreen(
    viewModel: DreamViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dream by viewModel.selectedDream.collectAsState()
    val detailTab by viewModel.detailTab.collectAsState()
    var isNarrativeExpanded by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (dream == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No dream selected", color = CosmicOnSurface)
        }
        return
    }

    val currentDream = dream!!

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .padding(bottom = 120.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CosmicOnSurfaceVariant
                        )
                    }
                    Column {
                        Text(
                            text = "Dream Detail",
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = CosmicOnSurface
                        )
                        Text(
                            text = "Dreamscape AI",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 10.sp,
                            color = CosmicTertiary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { viewModel.showImageVariations.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = "Variations",
                            tint = CosmicTertiary
                        )
                    }
                    IconButton(onClick = { viewModel.showShareExport.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CosmicPrimary
                        )
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Dream",
                            tint = CosmicError.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Sub-Navigation Tabs: Story | Details | Analysis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerHigh.copy(alpha = 0.8f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                        .padding(4.dp)
                ) {
                    DetailTabPill(
                        title = "Story",
                        isSelected = detailTab == DetailTab.STORY,
                        onClick = { viewModel.setDetailTab(DetailTab.STORY) }
                    )
                    DetailTabPill(
                        title = "Details",
                        isSelected = detailTab == DetailTab.DETAILS,
                        onClick = { viewModel.setDetailTab(DetailTab.DETAILS) }
                    )
                    DetailTabPill(
                        title = "Analysis",
                        isSelected = detailTab == DetailTab.ANALYSIS,
                        onClick = { viewModel.setDetailTab(DetailTab.ANALYSIS) }
                    )
                }
            }

            // Tab Content
            when (detailTab) {
                DetailTab.STORY -> {
                    StoryTabView(
                        dream = currentDream,
                        isExpanded = isNarrativeExpanded,
                        onToggleExpand = { isNarrativeExpanded = !isNarrativeExpanded },
                        onToggleFavorite = { viewModel.toggleFavorite(currentDream.id) },
                        onOpenVariations = { viewModel.showImageVariations.value = true },
                        onAddTag = { viewModel.showAddTagDialog.value = true }
                    )
                }
                DetailTab.DETAILS -> {
                    DetailsTabView(
                        dream = currentDream,
                        onOpenNotes = { viewModel.showBookmarkNotes.value = true }
                    )
                }
                DetailTab.ANALYSIS -> {
                    AnalysisTabView(dream = currentDream)
                }
            }
        }

        // Fixed Bottom Floating Dock Actions
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(CircleShape)
                .background(CosmicSurfaceContainerLowest.copy(alpha = 0.9f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Secondary Regenerate Button
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHigh)
                    .clickable { viewModel.regenerateImage(currentDream.id) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = CosmicTertiary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Regenerate",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CosmicOnSurface
                )
            }

            // Primary Save / Notes Button
            Row(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(CosmicPrimaryContainer, CosmicPrimary, AuroraPink)
                        )
                    )
                    .clickable { viewModel.showBookmarkNotes.value = true }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Save Dream",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

        if (showDeleteConfirmDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = {
                    Text(
                        text = "Dissolve Dream Vision?",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = CosmicOnSurface
                    )
                },
                text = {
                    Text(
                        text = "This dream and its AI subconscious analysis will be permanently deleted from your celestial vault and Firestore.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 13.sp,
                        color = CosmicOnSurfaceVariant
                    )
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showDeleteConfirmDialog = false
                            viewModel.deleteDream(currentDream.id)
                            onBack()
                        }
                    ) {
                        Text("Delete Permanently", color = CosmicError, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text("Keep", color = CosmicOnSurfaceVariant)
                    }
                },
                containerColor = CosmicSurfaceContainerLowest
            )
        }
    }
}

@Composable
fun StoryTabView(
    dream: Dream,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenVariations: () -> Unit,
    onAddTag: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Visual Dream Artwork Showcase
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(22.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
        ) {
            AsyncImage(
                model = dream.imageUrl,
                contentDescription = dream.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerLowest.copy(alpha = 0.75f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CosmicTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "DREAM CANVAS",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = CosmicOnSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerLowest.copy(alpha = 0.75f))
                        .clickable { onOpenVariations() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Variations",
                        tint = CosmicOnSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Bottom-Right Coherence Pill
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHigh.copy(alpha = 0.85f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
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
                    text = "${dream.coherencePercent}% Coherence",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = CosmicTertiary
                )
            }
        }

        // Title & Timestamp Block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dream.title,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = CosmicOnSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${dream.date} • ${dream.time} • ${dream.lucidityLevel}",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHigh.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = if (dream.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (dream.isFavorite) CosmicSecondary else CosmicOnSurfaceVariant
                )
            }
        }

        // Narrative Section Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainer.copy(alpha = 0.75f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = CosmicSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "AI TRANSCRIBED VISION",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = CosmicSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isExpanded) dream.expandedNarrative else dream.narrative,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = CosmicOnSurface.copy(alpha = 0.9f)
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onToggleExpand() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Read less" else "Read more",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = CosmicTertiary
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = CosmicTertiary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Tags & Emotional Archetypes
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "TAGS & EMOTIONAL ARCHETYPES",
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = CosmicOnSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dream.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CosmicSurfaceContainerHigh)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag,
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            color = CosmicPrimary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerHighest)
                        .clickable { onAddTag() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Tag",
                        tint = CosmicOnSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Subconscious Symbols
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainerLow)
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Subconscious Symbols",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = CosmicOnSurface
                    )
                }
                Text(
                    text = "${dream.symbols.size} Decoded",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    color = CosmicTertiary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dream.symbols.forEach { symbol ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CosmicSurfaceContainerHigh.copy(alpha = 0.6f))
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val icon = when (symbol.iconName) {
                            "flight" -> Icons.Default.Flight
                            "waterfall_chart" -> Icons.Default.WaterfallChart
                            else -> Icons.Default.Stars
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = CosmicSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = symbol.name,
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = CosmicOnSurface,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = symbol.meaning,
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 9.sp,
                            color = CosmicOnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsTabView(
    dream: Dream,
    onOpenNotes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Emotional Intensity Gauge Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainer.copy(alpha = 0.8f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dream Emotional Intensity",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CosmicOnSurface
                )
                Text(
                    text = "${dream.emotionIntensityPercent}%",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = CosmicSecondary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHighest)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(dream.emotionIntensityPercent / 100f)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(CosmicPrimary, CosmicSecondary, CosmicTertiary)
                            )
                        )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtle & Calm", fontSize = 10.sp, color = CosmicOnSurfaceVariant)
                Text(text = "Deep Immersion", fontSize = 10.sp, color = CosmicOnSurfaceVariant)
                Text(text = "Hyper-Vivid", fontSize = 10.sp, color = CosmicOnSurfaceVariant)
            }
        }

        // Category & Lucidity Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailInfoPill(
                title = "Category",
                value = dream.category,
                icon = Icons.Default.Stars,
                modifier = Modifier.weight(1f)
            )
            DetailInfoPill(
                title = "Lucidity",
                value = dream.lucidityLevel,
                icon = Icons.Default.Bedtime,
                modifier = Modifier.weight(1f)
            )
        }

        // Key Elements Breakdown Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainer.copy(alpha = 0.7f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "KEY ELEMENTS",
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = CosmicPrimary
            )

            KeyElementItem(
                icon = Icons.Default.Person,
                title = "Main Characters",
                description = dream.keyElements.mainCharacters,
                tint = CosmicTertiary
            )
            KeyElementItem(
                icon = Icons.Default.LocationOn,
                title = "Locations",
                description = dream.keyElements.locations,
                tint = CosmicSecondary
            )
            KeyElementItem(
                icon = Icons.Default.Token,
                title = "Artifacts & Objects",
                description = dream.keyElements.objects,
                tint = CosmicPrimary
            )
            KeyElementItem(
                icon = Icons.Default.PsychologyAlt,
                title = "Recurring Themes",
                description = dream.keyElements.themes,
                tint = CosmicTertiary
            )
        }

        // Personal Note Peek Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CosmicSurfaceContainerLow)
                .clickable { onOpenNotes() }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Personal Note & Reflections",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CosmicOnSurface
                )
                Text(
                    text = if (dream.personalNotes.isBlank()) "Tap to add private journal notes..." else dream.personalNotes,
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    color = CosmicOnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Notes",
                tint = CosmicPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun AnalysisTabView(dream: Dream) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Interpretation Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainer.copy(alpha = 0.8f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = CosmicSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Dream Interpretation",
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CosmicOnSurface
                )
            }
            Text(
                text = dream.interpretation,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = CosmicOnSurface.copy(alpha = 0.9f)
            )
        }

        // Sleep State & Circadian Insights Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CosmicSurfaceContainerLow)
                    .padding(14.dp)
            ) {
                Text(
                    text = "Estimated REM Time",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    color = CosmicOnSurfaceVariant
                )
                Text(
                    text = dream.estimatedRemTime,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CosmicTertiary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Deep Stage REM",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 10.sp,
                    color = CosmicOnSurface.copy(alpha = 0.6f)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CosmicSurfaceContainerLow)
                    .padding(14.dp)
            ) {
                Text(
                    text = "Clarity Index",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    color = CosmicOnSurfaceVariant
                )
                Text(
                    text = dream.clarityScore,
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CosmicSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "High Recall Detail",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 10.sp,
                    color = CosmicOnSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Jungian Shadow / Archetype Insight
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainerHigh.copy(alpha = 0.65f))
                .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Diamond,
                contentDescription = null,
                tint = CosmicPrimary,
                modifier = Modifier.size(24.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${dream.archetype} Detected",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CosmicOnSurface
                )
                Text(
                    text = "The cloaked entity and elevated topography suggest readiness to absorb profound wisdom or navigate a momentous transition.",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = CosmicOnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DetailTabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isSelected) Brush.horizontalGradient(
                    listOf(CosmicPrimaryContainer, CosmicSecondaryContainer)
                )
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            )
            .clickable { onClick() }
            .padding(horizontal = 22.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = PlusJakartaFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else CosmicOnSurfaceVariant
        )
    }
}

@Composable
fun DetailInfoPill(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CosmicSurfaceContainerLow)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CosmicPrimaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CosmicPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text = title,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                color = CosmicOnSurfaceVariant
            )
            Text(
                text = value,
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = CosmicOnSurface
            )
        }
    }
}

@Composable
fun KeyElementItem(
    icon: ImageVector,
    title: String,
    description: String,
    tint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
        }
        Column {
            Text(
                text = title,
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = CosmicOnSurface
            )
            Text(
                text = description,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 11.sp,
                color = CosmicOnSurfaceVariant
            )
        }
    }
}
