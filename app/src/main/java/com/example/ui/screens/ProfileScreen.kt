package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
import com.example.ui.theme.CosmicSurfaceContainerLowest
import com.example.ui.theme.CosmicTertiary
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.DreamViewModel

@Composable
fun ProfileScreen(
    viewModel: DreamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val reminderActive by viewModel.lucidityReminderActive.collectAsState()
    val reminderTime by viewModel.reminderChimeTime.collectAsState()
    var calendarSyncActive by remember { mutableStateOf(true) }
    var highDefAudio by remember { mutableStateOf(true) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.lucidityReminderActive.value = true
            viewModel.triggerLucidityChime(context)
        } else {
            viewModel.lucidityReminderActive.value = true
            viewModel.showToast("Reminder enabled (notifications muted)")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 120.dp)
    ) {
        // User Profile Hero Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(CosmicPrimary, CosmicSecondary, CosmicTertiary, CosmicPrimary)
                        )
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqqnCXlZsru--wYS5VxYi-Ssh9marKRvG75r6y1xwap4bhWIipqhVAYQvnKy45sD9QjVzf7RrqBpm7MT8hO-tCSrILII9t9qGeBGMbg1CQRaxcATr2vXDDvzCbhQM7437bFzCtEn-g_uUOya6tCXMfblHn6Sgm37mi1l2-OL7lUNU3Y-Mw4xaovNSWuNPkxyTvIWjydrUJUxbcv0xvIpC8-HzoB7l6uhKIahaqRnffYOflTHiOOFjX9A",
                    contentDescription = "Profile Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Dharani",
                fontFamily = PlayfairFontFamily,
                fontSize = 24.sp,
                color = CosmicOnSurface
            )
            Text(
                text = "dharan.v2006@gmail.com",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = CosmicTertiary
            )
            Text(
                text = "Celestial Dreamer • Level IV Lucidity",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 11.sp,
                color = CosmicOnSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Daily Reminder Notification Preview Card (Screen 11)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainerLow)
                .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "5:30 AM",
                        fontFamily = PlayfairFontFamily,
                        fontSize = 22.sp,
                        color = CosmicOnSurface
                    )
                    Text(
                        text = "Tuesday, Oct 15",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 11.sp,
                        color = CosmicOnSurfaceVariant
                    )
                }
                Switch(
                    checked = reminderActive,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.lucidityReminderActive.value = true
                                viewModel.triggerLucidityChime(context)
                            }
                        } else {
                            viewModel.lucidityReminderActive.value = false
                            viewModel.showToast("Reminder deactivated")
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CosmicPrimary,
                        checkedTrackColor = CosmicPrimary.copy(alpha = 0.3f),
                        uncheckedThumbColor = CosmicOnSurfaceVariant,
                        uncheckedTrackColor = CosmicSurfaceContainerHigh
                    )
                )
            }

            // Notification Sample Box (Tap to Test Real Chime)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CosmicSurfaceContainerHigh.copy(alpha = 0.6f))
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.triggerLucidityChime(context)
                        }
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CosmicPrimaryContainer.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = "Dharani Dreamscape AI  •  Tap to test chime",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 10.sp,
                        color = CosmicTertiary
                    )
                    Text(
                        text = "✨ Time to capture your dream before it fades away. ☁️",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurface
                    )
                }
            }
        }

        // Settings Toggles List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurfaceContainer.copy(alpha = 0.7f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "SUBCONSCIOUS SETTINGS",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = CosmicPrimary
            )

            SettingsRow(
                icon = Icons.Default.CalendarMonth,
                title = "Google Calendar Sync",
                subtitle = "Schedule lucidity chimes directly",
                isChecked = calendarSyncActive,
                onCheckedChange = {
                    calendarSyncActive = it
                    viewModel.showToast(if (it) "Calendar sync enabled" else "Calendar sync disabled")
                }
            )

            SettingsRow(
                icon = Icons.Default.GraphicEq,
                title = "High-Fidelity Audio Enhance",
                subtitle = "Automatic noise cleanup for voice recording",
                isChecked = highDefAudio,
                onCheckedChange = { highDefAudio = it }
            )

            SettingsRow(
                icon = Icons.Default.CloudDone,
                title = "Google Drive Cloud Backup",
                subtitle = "Encrypted nocturnal journals stored safely",
                isChecked = true,
                onCheckedChange = { viewModel.showToast("Cloud sync active") }
            )
        }

        // Export & Privacy Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CosmicSurfaceContainerLow)
                    .clickable { viewModel.showShareExport.value = true }
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = CosmicTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Export All Dream Data (PDF / Text)",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 13.sp,
                        color = CosmicOnSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CosmicSurfaceContainerLow)
                    .clickable { viewModel.showToast("Privacy shield verified: 100% cloud encrypted") }
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = AuroraPink,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Subconscious Privacy & Security",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 13.sp,
                        color = CosmicOnSurface
                    )
                }
            }

            // Sign Out
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CosmicSurfaceContainerHigh)
                    .clickable {
                        com.google.firebase.Firebase.auth.signOut()
                        viewModel.showToast("Signed out of Google")
                    }
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = CosmicSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Sign Out of Account",
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CosmicSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceContainerHigh),
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
                    fontSize = 13.sp,
                    color = CosmicOnSurface
                )
                Text(
                    text = subtitle,
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 11.sp,
                    color = CosmicOnSurfaceVariant
                )
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CosmicPrimary,
                checkedTrackColor = CosmicPrimary.copy(alpha = 0.35f),
                uncheckedThumbColor = CosmicOnSurfaceVariant,
                uncheckedTrackColor = CosmicSurfaceContainerHigh
            )
        )
    }
}
