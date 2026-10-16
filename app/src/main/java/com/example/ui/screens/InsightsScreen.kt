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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.CosmicSurfaceContainerLow
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.CosmicTertiaryContainer
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.AppTab
import com.example.viewmodel.DreamViewModel

@Composable
fun InsightsScreen(
    viewModel: DreamViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    tint = CosmicSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = "Subconscious Insights",
                        fontFamily = PlayfairFontFamily,
                        fontSize = 20.sp,
                        color = CosmicOnSurface
                    )
                    Text(
                        text = "NEUROLOGICAL & SYMBOLIC PATTERNS",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = CosmicTertiary
                    )
                }
            }

            // Quick Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard(
                    title = "Total Dreams",
                    value = "58",
                    subtitle = "+4 this week",
                    icon = Icons.Default.Bedtime,
                    tint = CosmicPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Lucidity Rate",
                    value = "68%",
                    subtitle = "High awareness",
                    icon = Icons.Default.AutoAwesome,
                    tint = CosmicTertiary,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard(
                    title = "Avg. Recall",
                    value = "8.4 / 10",
                    subtitle = "Vivid memory",
                    icon = Icons.Default.Psychology,
                    tint = CosmicSecondary,
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Streak Record",
                    value = "12 Days",
                    subtitle = "Synaptic active",
                    icon = Icons.Default.LocalFireDepartment,
                    tint = AuroraPink,
                    modifier = Modifier.weight(1f)
                )
            }

            // Recurring Archetypes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurfaceContainerLow)
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "DOMINANT DREAM ARCHETYPES",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = CosmicPrimary
                )

                ArchetypeBar(label = "Flight & Levitation", percentage = 42, color = CosmicPrimary)
                ArchetypeBar(label = "Floating Citadels & Spires", percentage = 31, color = CosmicSecondary)
                ArchetypeBar(label = "Water & Bioluminescence", percentage = 27, color = CosmicTertiary)
                ArchetypeBar(label = "Ancient Wisdom Wisps", percentage = 18, color = AuroraPink)
            }

            // Interactive Dream Map Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(CosmicSurfaceContainerLow, CosmicSurfaceContainerHigh)
                        )
                    )
                    .border(1.dp, CosmicTertiary.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .clickable { viewModel.showDreamMap.value = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Explore Dream Map",
                        fontFamily = PlayfairFontFamily,
                        fontSize = 16.sp,
                        color = CosmicOnSurface
                    )
                    Text(
                        text = "View nodes and topography connecting your dream locations.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 11.sp,
                        color = CosmicOnSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CosmicTertiaryContainer.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Hub,
                        contentDescription = null,
                        tint = CosmicTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Weekly Subconscious Synthesis Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurfaceContainer)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterfallChart,
                        contentDescription = null,
                        tint = CosmicTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Weekly Subconscious Report",
                        fontFamily = PlayfairFontFamily,
                        fontSize = 16.sp,
                        color = CosmicOnSurface
                    )
                }
                Text(
                    text = "This week exhibited a 22% increase in lucid control. Your dreams consistently gravitated toward celestial heights and expansive vistas, reflecting freedom and confidence in current life decisions.",
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
fun StatMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CosmicSurfaceContainerLow)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
            Text(
                text = subtitle,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                color = CosmicOnSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = value,
            fontFamily = PlayfairFontFamily,
            fontSize = 22.sp,
            color = CosmicOnSurface
        )
        Text(
            text = title,
            fontFamily = PlusJakartaFontFamily,
            fontSize = 11.sp,
            color = CosmicOnSurfaceVariant
        )
    }
}

@Composable
fun ArchetypeBar(label: String, percentage: Int, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = CosmicOnSurface
            )
            Text(
                text = "$percentage%",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = color
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(CosmicSurfaceContainerHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
