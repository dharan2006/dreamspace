package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CelestialCyan
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicOnSurface
import com.example.ui.theme.CosmicOnSurfaceVariant
import com.example.ui.theme.CosmicPrimary
import com.example.ui.theme.CosmicPrimaryContainer
import com.example.ui.theme.CosmicSecondary
import com.example.ui.theme.CosmicSurfaceContainer
import com.example.ui.theme.CosmicSurfaceContainerHigh
import com.example.ui.theme.CosmicSurfaceContainerLow
import com.example.ui.theme.CosmicSurfaceContainerLowest
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.AppTab

@Composable
fun AmbientAuraBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
    ) {
        // Top ambient violet glow orb
        Box(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-60).dp)
                .blur(90.dp)
                .background(CosmicPrimary.copy(alpha = 0.18f), CircleShape)
        )
        // Mid-left aurora pink orb
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-80).dp, y = 50.dp)
                .blur(85.dp)
                .background(CosmicSecondary.copy(alpha = 0.14f), CircleShape)
        )
        // Lower-right celestial cyan glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .blur(90.dp)
                .background(CosmicTertiary.copy(alpha = 0.14f), CircleShape)
        )

        content()
    }
}

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ethereal crescent logo icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(CosmicSurfaceContainerHigh, CosmicSurfaceContainerLowest)
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(CosmicPrimary, CosmicSecondary)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Logo",
                    tint = CosmicPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title ?: "Dharani",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = CosmicOnSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Dreamscape",
                        fontFamily = PlayfairFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = CosmicPrimary
                    )
                }
                Text(
                    text = (subtitle ?: "Celestial Subconscious").uppercase(),
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    letterSpacing = 1.2.sp,
                    color = CosmicTertiary
                )
            }
        }

        // Profile Avatar with dynamic gradient ring
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(CosmicPrimary, CosmicSecondary, CosmicTertiary, CosmicPrimary)
                    )
                )
                .padding(2.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() }
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqqnCXlZsru--wYS5VxYi-Ssh9marKRvG75r6y1xwap4bhWIipqhVAYQvnKy45sD9QjVzf7RrqBpm7MT8hO-tCSrILII9t9qGeBGMbg1CQRaxcATr2vXDDvzCbhQM7437bFzCtEn-g_uUOya6tCXMfblHn6Sgm37mi1l2-OL7lUNU3Y-Mw4xaovNSWuNPkxyTvIWjydrUJUxbcv0xvIpC8-HzoB7l6uhKIahaqRnffYOflTHiOOFjX9A",
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Online status indicator dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(CosmicTertiary)
                    .border(1.dp, CosmicBackground, CircleShape)
            )
        }
    }
}

@Composable
fun FloatingGlassDock(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    onCaptureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Dock Capsule background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(CircleShape)
                .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DockTabItem(
                label = "Home",
                icon = Icons.Default.AutoAwesome,
                isSelected = currentTab == AppTab.HOME,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(AppTab.HOME) }
            )

            DockTabItem(
                label = "Library",
                icon = Icons.Default.AutoStories,
                isSelected = currentTab == AppTab.LIBRARY,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(AppTab.LIBRARY) }
            )

            // Center Spacer for elevated Mic Button
            Spacer(modifier = Modifier.weight(1.2f))

            DockTabItem(
                label = "Insights",
                icon = Icons.Default.Insights,
                isSelected = currentTab == AppTab.INSIGHTS,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(AppTab.INSIGHTS) }
            )

            DockTabItem(
                label = "Profile",
                icon = Icons.Default.AccountCircle,
                isSelected = currentTab == AppTab.PROFILE,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(AppTab.PROFILE) }
            )
        }

        // Floating Elevated Mic Capture Action Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-14).dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(NeonViolet, CosmicPrimaryContainer, AuroraPink)
                    )
                )
                .border(1.5.dp, Color.White.copy(alpha = 0.45f), CircleShape)
                .clickable { onCaptureClick() }
                .testTag("floating_capture_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Capture Dream",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun DockTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) CosmicSurfaceContainerHigh.copy(alpha = 0.9f)
                else Color.Transparent
            )
            .clickable { onClick() }
            .testTag("tab_$label"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CosmicPrimary else CosmicOnSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CosmicPrimary else CosmicOnSurfaceVariant
            )
        }
    }
}

@Composable
fun FloatingToast(
    message: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
        modifier = modifier
    ) {
        if (message != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainer.copy(alpha = 0.95f))
                    .border(1.dp, CosmicPrimary.copy(alpha = 0.35f), CircleShape)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = message,
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = CosmicOnSurface
                    )
                }
            }
        }
    }
}
