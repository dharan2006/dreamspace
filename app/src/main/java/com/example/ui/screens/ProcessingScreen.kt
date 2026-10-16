package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicOnSurface
import com.example.ui.theme.CosmicOnSurfaceVariant
import com.example.ui.theme.CosmicPrimary
import com.example.ui.theme.CosmicPrimaryContainer
import com.example.ui.theme.CosmicSecondary
import com.example.ui.theme.CosmicSurfaceContainer
import com.example.ui.theme.CosmicSurfaceContainerHigh
import com.example.ui.theme.CosmicSurfaceContainerLow
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.DreamViewModel

@Composable
fun ProcessingScreen(
    viewModel: DreamViewModel,
    modifier: Modifier = Modifier
) {
    val currentStep by viewModel.processingStep.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "processing_rot")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    val steps = listOf(
        "Converting speech to text...",
        "Enhancing your dream story...",
        "Analyzing emotions...",
        "Extracting key elements...",
        "Generating dream image..."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.closeCaptureScreen() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CosmicOnSurfaceVariant
                )
            }
            Text(
                text = "Processing Your Dream",
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CosmicOnSurface
            )
            Icon(
                imageVector = Icons.Default.NightlightRound,
                contentDescription = null,
                tint = CosmicPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Cosmic Dream Cloud Animation
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Rotating cosmic particle orbit
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .rotate(rotationAngle)
                    .border(
                        1.5.dp,
                        Brush.sweepGradient(
                            listOf(CosmicPrimary, Color.Transparent, CosmicSecondary, Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            // Inner glowing cloud container
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(CosmicSurfaceContainerHigh, CosmicSurfaceContainer)
                        )
                    )
                    .border(1.dp, CosmicPrimary.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "☁️", fontSize = 48.sp)
                    Text(
                        text = "AI Dreaming...",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = CosmicPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sequential Step Checklist with Glowing Checks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            steps.forEachIndexed { index, label ->
                val stepNum = index + 1
                val isCompleted = currentStep > stepNum
                val isInProgress = currentStep == stepNum

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> CosmicPrimary
                                    isInProgress -> CosmicPrimaryContainer.copy(alpha = 0.4f)
                                    else -> CosmicSurfaceContainerHigh
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = CosmicBackground,
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (isInProgress) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = CosmicPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CosmicOnSurfaceVariant.copy(alpha = 0.4f))
                            )
                        }
                    }

                    Text(
                        text = label,
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = if (isInProgress || isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp,
                        color = if (isCompleted) CosmicOnSurface
                        else if (isInProgress) CosmicPrimary
                        else CosmicOnSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Quote Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(NeonViolet, AuroraPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = "“Turning your dreams into a beautiful story...”",
                fontFamily = PlusJakartaFontFamily,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = CosmicOnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
