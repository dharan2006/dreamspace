package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.CaptureMode
import com.example.viewmodel.DreamViewModel
import com.example.viewmodel.RecordingState

@Composable
fun CaptureScreen(
    viewModel: DreamViewModel,
    modifier: Modifier = Modifier
) {
    val captureMode by viewModel.captureMode.collectAsState()
    val recordingState by viewModel.recordingState.collectAsState()
    val recordingSeconds by viewModel.recordingSeconds.collectAsState()
    val waveformHeights by viewModel.waveformHeights.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    var showLanguageMenu by remember { mutableStateOf(false) }
    val languages = listOf(
        "Default - English (Auto Detect)",
        "Tamil (தமிழ்)",
        "English (US / UK)",
        "Spanish (Español)",
        "Japanese (日本語)"
    )

    // Pulsing aura animation when recording
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (recordingState == RecordingState.RECORDING) 1.15f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val context = LocalContext.current
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.startRecording(if (isGranted) context else null)
    }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.handleAudioImport(context, uri)
        }
    }

    val onToggleRecordAction: () -> Unit = {
        if (recordingState == RecordingState.IDLE) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                viewModel.startRecording(context)
            } else {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        } else {
            viewModel.toggleRecording()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        // Top Navigation Bar
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
                IconButton(
                    onClick = { viewModel.closeCaptureScreen() },
                    modifier = Modifier.testTag("capture_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CosmicOnSurfaceVariant
                    )
                }
                Column {
                    Text(
                        text = "Capture",
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
        }

        // Segmented Mode Switcher (Voice | Text | Details)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp)
                .clip(CircleShape)
                .background(CosmicSurfaceContainerLowest.copy(alpha = 0.85f))
                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CaptureModePill(
                title = "Voice",
                icon = Icons.Default.Mic,
                isSelected = captureMode == CaptureMode.VOICE,
                onClick = { viewModel.setCaptureMode(CaptureMode.VOICE) },
                modifier = Modifier.weight(1f)
            )
            CaptureModePill(
                title = "Text",
                icon = Icons.Default.EditNote,
                isSelected = captureMode == CaptureMode.TEXT,
                onClick = { viewModel.setCaptureMode(CaptureMode.TEXT) },
                modifier = Modifier.weight(1f)
            )
            CaptureModePill(
                title = "Details",
                icon = Icons.Default.Tune,
                isSelected = captureMode == CaptureMode.DETAILS,
                onClick = { viewModel.setCaptureMode(CaptureMode.DETAILS) },
                modifier = Modifier.weight(1f)
            )
        }

        when (captureMode) {
            CaptureMode.VOICE -> {
                // Central Interactive Voice Capture Portal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Concentric Animated Ripple Rings & Mic Orb
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .scale(pulseScale),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outermost ripple
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainerHigh.copy(alpha = 0.25f))
                                .border(1.dp, CosmicPrimary.copy(alpha = 0.15f), CircleShape)
                        )
                        // Middle ripple
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainer.copy(alpha = 0.45f))
                                .border(1.dp, CosmicSecondary.copy(alpha = 0.2f), CircleShape)
                        )
                        // Radiant glass well
                        Box(
                            modifier = Modifier
                                .size(145.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceBright.copy(alpha = 0.4f))
                                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        )
                        // Central Glowing Mic Orb Button
                        Box(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(NeonViolet, CosmicPrimaryContainer, AuroraPink)
                                    )
                                )
                                .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                .clickable { onToggleRecordAction() }
                                .testTag("main_mic_orb"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Toggle Recording",
                                    tint = Color.White,
                                    modifier = Modifier.size(44.dp)
                                )
                                Text(
                                    text = when (recordingState) {
                                        RecordingState.IDLE -> "TAP TO SPEAK"
                                        RecordingState.RECORDING -> "RECORDING"
                                        RecordingState.PAUSED -> "PAUSED"
                                    },
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.White.copy(alpha = 0.95f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status title & cue
                    Text(
                        text = when (recordingState) {
                            RecordingState.IDLE -> "Tap to Start Recording"
                            RecordingState.RECORDING -> "Listening to your dream..."
                            RecordingState.PAUSED -> "Recording Paused"
                        },
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = CosmicOnSurface
                    )
                    Text(
                        text = "Speak naturally about your dream... I'll convert it to text and enhance it.",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(280.dp)
                    )

                    // Digital Timer Pill
                    val minutes = String.format("%02d", recordingSeconds / 60)
                    val seconds = String.format("%02d", recordingSeconds % 60)
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clip(CircleShape)
                            .background(CosmicSurfaceContainerLow.copy(alpha = 0.85f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (recordingState == RecordingState.RECORDING) CosmicTertiary
                                    else CosmicError
                                )
                        )
                        Text(
                            text = "$minutes:$seconds",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = CosmicOnSurface,
                            letterSpacing = 2.sp
                        )
                    }

                    // Real-Time Waveform Visualizer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        waveformHeights.forEachIndexed { index, height ->
                            val color = when {
                                index % 3 == 0 -> CosmicTertiary
                                index % 3 == 1 -> CosmicPrimary
                                else -> CosmicSecondary
                            }
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(height.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }

                    // Action Control Cluster (Pause / Record Trigger / Finish)
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pause / Resume Button
                        IconButton(
                            onClick = {
                                if (recordingState == RecordingState.RECORDING) viewModel.pauseRecording()
                                else if (recordingState == RecordingState.PAUSED) viewModel.resumeRecording()
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainerHigh.copy(alpha = 0.8f))
                        ) {
                            Icon(
                                imageVector = if (recordingState == RecordingState.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Pause/Resume",
                                tint = CosmicOnSurface
                            )
                        }

                        // Big Glowing Center Trigger
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CosmicSecondaryContainer, CosmicPrimaryContainer)
                                    )
                                )
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainerLowest)
                                .clickable { onToggleRecordAction() },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(
                                        if (recordingState == RecordingState.RECORDING) RoundedCornerShape(6.dp)
                                        else CircleShape
                                    )
                                    .background(CosmicSecondary)
                            )
                        }

                        // Stop & Process Button
                        IconButton(
                            onClick = { viewModel.stopRecordingAndProcess() },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainerHigh.copy(alpha = 0.8f))
                                .testTag("stop_and_process_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop & Process",
                                tint = CosmicSecondary
                            )
                        }
                    }

                    // Language Selector Dropdown
                    Box(modifier = Modifier.padding(top = 16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .clip(CircleShape)
                                .background(CosmicSurfaceContainer.copy(alpha = 0.8f))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                .clickable { showLanguageMenu = true }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = CosmicTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = selectedLanguage,
                                    fontFamily = PlusJakartaFontFamily,
                                    fontSize = 12.sp,
                                    color = CosmicOnSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = CosmicOnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false },
                            modifier = Modifier.background(CosmicSurfaceContainerHigh)
                        ) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = lang,
                                            fontFamily = PlusJakartaFontFamily,
                                            fontSize = 12.sp,
                                            color = CosmicOnSurface
                                        )
                                    },
                                    onClick = {
                                        showLanguageMenu = false
                                        viewModel.showToast("Language set to $lang")
                                    }
                                )
                            }
                        }
                    }

                    // Quick Alternative Capture Shortcuts
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CosmicSurfaceContainerLow.copy(alpha = 0.7f))
                                .clickable { viewModel.setCaptureMode(CaptureMode.TEXT) }
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Keyboard,
                                contentDescription = null,
                                tint = CosmicPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Type manually",
                                fontFamily = PlusJakartaFontFamily,
                                fontSize = 11.sp,
                                color = CosmicOnSurfaceVariant
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CosmicSurfaceContainerLow.copy(alpha = 0.7f))
                                .clickable {
                                    audioPickerLauncher.launch("audio/*")
                                }
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = CosmicTertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Upload audio",
                                fontFamily = PlusJakartaFontFamily,
                                fontSize = 11.sp,
                                color = CosmicOnSurfaceVariant
                            )
                        }
                    }

                    // Celestial Guidance Quote Pill Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(CosmicSurfaceContainerLow.copy(alpha = 0.8f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(18.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CosmicPrimary, CosmicSecondary)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "“Turning your dreams into a beautiful story...”",
                            fontFamily = PlusJakartaFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            color = CosmicOnSurfaceVariant
                        )
                    }
                }
            }

            CaptureMode.TEXT -> {
                // Manual Text Journaling Mode
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val draftTitleState by viewModel.draftTitle.collectAsState()
                    val draftTranscriptState by viewModel.draftTranscript.collectAsState()

                    Text(
                        text = "Describe Your Dream",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CosmicOnSurface
                    )

                    OutlinedTextField(
                        value = draftTitleState,
                        onValueChange = { viewModel.draftTitle.value = it },
                        label = { Text("Dream Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CosmicPrimary,
                            unfocusedBorderColor = CosmicSurfaceContainerHighest,
                            focusedTextColor = CosmicOnSurface,
                            unfocusedTextColor = CosmicOnSurface
                        )
                    )

                    OutlinedTextField(
                        value = draftTranscriptState,
                        onValueChange = { viewModel.draftTranscript.value = it },
                        label = { Text("What happened in your subconscious?") },
                        minLines = 6,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CosmicPrimary,
                            unfocusedBorderColor = CosmicSurfaceContainerHighest,
                            focusedTextColor = CosmicOnSurface,
                            unfocusedTextColor = CosmicOnSurface
                        )
                    )

                    // Enhance and Generate CTA
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(CosmicPrimary, AuroraPink)
                                )
                            )
                            .clickable { viewModel.launchProcessingFlow() }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PROCESS & VISUALIZE WITH AI",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }

            CaptureMode.DETAILS -> {
                // Add Details Screen (Screen 7 in user designs)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val draftTitleState by viewModel.draftTitle.collectAsState()
                    val draftMoodState by viewModel.draftMood.collectAsState()
                    val draftTagsState by viewModel.draftTags.collectAsState()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Details",
                            fontFamily = PlayfairFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = CosmicOnSurface
                        )
                        Text(
                            text = "2 / 3",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CosmicTertiary
                        )
                    }

                    // Dream Title
                    Text(
                        text = "Dream Title",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = draftTitleState,
                        onValueChange = { viewModel.draftTitle.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CosmicPrimary,
                            unfocusedBorderColor = CosmicSurfaceContainerHighest,
                            focusedTextColor = CosmicOnSurface,
                            unfocusedTextColor = CosmicOnSurface
                        )
                    )

                    // Date & Time Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CosmicSurfaceContainerLow)
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "📅 Oct 15, 2026",
                                fontFamily = PlusJakartaFontFamily,
                                fontSize = 13.sp,
                                color = CosmicOnSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CosmicSurfaceContainerLow)
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "⏰ 5:42 AM",
                                fontFamily = PlusJakartaFontFamily,
                                fontSize = 13.sp,
                                color = CosmicOnSurface
                            )
                        }
                    }

                    // Mood Selector (Emoji Row)
                    Text(
                        text = "Dream Mood",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val moods = listOf(
                            "Wonder" to "✨",
                            "Euphoric" to "🥰",
                            "Serene" to "😌",
                            "Intrigue" to "🤔",
                            "Turbulent" to "⚡"
                        )
                        moods.forEach { (m, emoji) ->
                            val isSelected = draftMoodState == m
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) CosmicPrimaryContainer.copy(alpha = 0.5f)
                                        else CosmicSurfaceContainerLow
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) CosmicPrimary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { viewModel.draftMood.value = m },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 22.sp)
                            }
                        }
                    }

                    // Tags
                    Text(
                        text = "Tags",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        draftTagsState.take(3).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(CosmicSurfaceContainerHigh)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontFamily = PlusJakartaFontFamily,
                                    fontSize = 11.sp,
                                    color = CosmicOnSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Finalize button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(CosmicPrimary, AuroraPink)
                                )
                            )
                            .clickable { viewModel.launchProcessingFlow() }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FINALIZE & SAVE DREAM",
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CaptureModePill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                if (isSelected) Brush.horizontalGradient(
                    listOf(CosmicPrimaryContainer, CosmicSecondaryContainer)
                )
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else CosmicOnSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                fontFamily = PlusJakartaFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else CosmicOnSurfaceVariant
            )
        }
    }
}
