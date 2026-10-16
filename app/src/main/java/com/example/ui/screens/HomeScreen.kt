package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.AppHeader
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CelestialCyan
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
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.AppTab
import com.example.viewmodel.DreamViewModel

@Composable
fun HomeScreen(
    viewModel: DreamViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val reminderActive by viewModel.lucidityReminderActive.collectAsState()
    val reminderTime by viewModel.reminderChimeTime.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 100.dp)
    ) {
        AppHeader(
            onProfileClick = { viewModel.selectTab(AppTab.PROFILE) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cosmic Subconscious Dawn Header
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SUBCONSCIOUS DAWN",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 2.sp,
                        color = CosmicTertiary
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Morning,",
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = CosmicOnSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Dharani",
                                fontFamily = PlayfairFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                fontSize = 28.sp,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "☁️", fontSize = 22.sp)
                        }
                    }

                    // Waxing Moon Phase pill
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CosmicSurfaceContainerHigh.copy(alpha = 0.8f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NightlightRound,
                            contentDescription = "Moon Phase",
                            tint = CosmicSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Waxing 84%",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = CosmicOnSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "Every dream is a message from your deeper self.",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 14.sp,
                    color = CosmicOnSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            // Hero Visual Sanctuary: Dream Portal Artwork Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CosmicSurfaceContainerLowest)
                    .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                    .clickable { viewModel.openDreamDetail("dream-1") }
                    .testTag("hero_dream_portal")
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAE8xKGbnM8YGXy23pDwhOy1LI5NU0R6S2AZFaSn_a8CFRdlNZsQURvm1WPyz5ArFaEQ5Tb5HAhcI1hewuw5drTAgyYOOtnBqnbWpmgAO2B31_Mic-UUHE_byIMykKFg2_CSiP8uR12zHUjkvFzpAkbpF1BQ9XcDosNilSQJoE685WYHgo4xAnBQJyT6sG2VBd9RHBdXpkxMLQoX_YyL-K3NalCdre1P_elbbin93haXTyV9oOWPFT9fg",
                    contentDescription = "The Floating Citadels Artwork",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark subtle scrim gradients
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Transparent,
                                    CosmicSurfaceContainerLowest.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Top badge row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CosmicSurfaceContainerLowest.copy(alpha = 0.75f))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CosmicPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "VISION OF THE NIGHT",
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
                            .background(CosmicSurfaceContainerLowest.copy(alpha = 0.75f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInFull,
                            contentDescription = "Expand",
                            tint = CosmicOnSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Bottom Floating Narrative Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Landscape,
                            contentDescription = null,
                            tint = CosmicTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "DREAM PORTAL #142",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            color = CosmicTertiary
                        )
                    }
                    Text(
                        text = "The Floating Citadels",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CosmicOnSurface,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Suspended over violet auroras with cascading liquid starlight and obsidian bridges.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Quote of the Day Glass Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CosmicPrimaryContainer.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "“The mind wanders into the infinite cosmos when asleep.”",
                        fontFamily = PlayfairFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = CosmicOnSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Carl Jung • Dream Lexicon",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = CosmicTertiary
                    )
                }
            }

            // Subconscious Analytics: Dream Streak & Last Dream Split Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Streak Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                        .border(1.dp, CosmicSecondary.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CosmicSecondaryContainer, AuroraPink)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "ACTIVE",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp,
                            color = CosmicSecondary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CosmicSecondaryContainer.copy(alpha = 0.35f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Dream Streak",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "12",
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = CosmicOnSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "days",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 14.sp,
                            color = CosmicPrimary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    // Progress line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(CosmicSurfaceContainerHigh)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(CosmicSecondary, CosmicPrimary)
                                    )
                                )
                        )
                    }
                }

                // Last Dream Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                        .border(1.dp, CosmicTertiary.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                        .clickable { viewModel.openDreamDetail("dream-1") }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Insights,
                                contentDescription = null,
                                tint = CosmicTertiary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Oct 14, 2026",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 10.sp,
                            color = CosmicOnSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Last Dream",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                    Text(
                        text = "Floating City...",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CosmicOnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "A mysterious citadel with pink clouds.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 11.sp,
                        color = CosmicOnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = CosmicTertiary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "88% Recall",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 11.sp,
                            color = CosmicTertiary
                        )
                    }
                }
            }

            // Glowing Primary Action CTA: Capture Dream Portal Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(CosmicPrimary, AuroraPink, CelestialCyan)
                        )
                    )
                    .padding(1.5.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.openCaptureScreen() }
                    .testTag("capture_today_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(CosmicPrimaryContainer, CosmicSecondaryContainer)
                            )
                        )
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "CAPTURE TODAY'S DREAM",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Astral Archives: 4 Core Modules Grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CosmicPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Astral Archives",
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = CosmicOnSurface
                        )
                    }
                    Text(
                        text = "View All",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = CosmicTertiary,
                        modifier = Modifier.clickable { viewModel.selectTab(AppTab.LIBRARY) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AstralModuleCard(
                        title = "My Dreams",
                        subtitle = "58 logged journals",
                        icon = Icons.Default.AutoStories,
                        tint = CosmicPrimary,
                        containerColor = CosmicPrimaryContainer.copy(alpha = 0.25f),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(AppTab.LIBRARY) }
                    )
                    AstralModuleCard(
                        title = "Insights",
                        subtitle = "Themes & archetypes",
                        icon = Icons.Default.Insights,
                        tint = CosmicSecondary,
                        containerColor = CosmicSecondaryContainer.copy(alpha = 0.25f),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(AppTab.INSIGHTS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AstralModuleCard(
                        title = "Dream Map",
                        subtitle = "Topography & nodes",
                        icon = Icons.Default.Hub,
                        tint = CosmicTertiary,
                        containerColor = CosmicTertiaryContainer.copy(alpha = 0.25f),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.showDreamMap.value = true }
                    )
                    AstralModuleCard(
                        title = "Reports",
                        subtitle = "Weekly subconscious",
                        icon = Icons.Default.Assignment,
                        tint = CosmicPrimary,
                        containerColor = CosmicSurfaceContainerHigh,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(AppTab.INSIGHTS) }
                    )
                }
            }

            // Ambient Micro Habit Card: Lucidity Reminder Set
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
                    .clickable {
                        viewModel.lucidityReminderActive.value = !reminderActive
                        viewModel.showToast(
                            if (!reminderActive) "Lucidity chime enabled for $reminderTime"
                            else "Reminder paused"
                        )
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CosmicTertiaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bedtime,
                            contentDescription = null,
                            tint = CosmicTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Lucidity Reminder Set",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = CosmicOnSurface
                        )
                        Text(
                            text = "Daily chime at $reminderTime",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            color = CosmicOnSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (reminderActive) CosmicSurfaceContainerHighest
                            else CosmicSurfaceContainerLow
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (reminderActive) "ACTIVE" else "OFF",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = if (reminderActive) CosmicPrimary else CosmicOnSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (reminderActive) CosmicPrimary else CosmicOnSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AstralModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CosmicSurfaceContainerLow.copy(alpha = 0.8f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = CosmicOnSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = CosmicOnSurface
        )
        Text(
            text = subtitle,
            fontFamily = PlusJakartaFontFamily,
            fontSize = 11.sp,
            color = CosmicOnSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
