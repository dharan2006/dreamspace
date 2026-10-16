package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.AuroraPink
import com.example.ui.theme.CosmicBackground
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
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.viewmodel.DreamViewModel

@Composable
fun ImageVariationsDialog(
    viewModel: DreamViewModel,
    onDismiss: () -> Unit
) {
    val dream by viewModel.selectedDream.collectAsState()
    if (dream == null) return

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicPrimary.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI Image Variations",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CosmicOnSurface
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CosmicOnSurfaceVariant
                    )
                }
            }

            Text(
                text = "Tap any generated celestial variation to set as primary canvas for '${dream!!.title}'.",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = CosmicOnSurfaceVariant
            )

            // Grid of variations
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(280.dp)
            ) {
                items(dream!!.imageVariations) { imgUrl ->
                    val isSelected = dream!!.imageUrl == imgUrl
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                2.dp,
                                if (isSelected) CosmicPrimary else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                viewModel.selectImageVariation(dream!!.id, imgUrl)
                            }
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Variation",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .background(CosmicPrimary)
                                    .padding(4.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = "Active",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CosmicBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarkNotesDialog(
    viewModel: DreamViewModel,
    onDismiss: () -> Unit
) {
    val dream by viewModel.selectedDream.collectAsState()
    if (dream == null) return

    var notesText by remember(dream) { mutableStateOf(dream!!.personalNotes) }
    var isFavorite by remember(dream) { mutableStateOf(dream!!.isFavorite) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicSecondary.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = CosmicSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Bookmark & Notes",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CosmicOnSurface
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CosmicOnSurfaceVariant
                    )
                }
            }

            Text(
                text = "Personal Notes",
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = CosmicOnSurfaceVariant
            )

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                minLines = 4,
                placeholder = {
                    Text(
                        text = "This dream felt very real. I want to remember the feeling of freedom in this dream...",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 12.sp,
                        color = CosmicOnSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CosmicSecondary,
                    unfocusedBorderColor = CosmicSurfaceContainerHighest,
                    focusedTextColor = CosmicOnSurface,
                    unfocusedTextColor = CosmicOnSurface
                )
            )

            // Favorite Dream Toggle Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CosmicSurfaceContainerLow)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = CosmicSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Favorite Dream",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 13.sp,
                        color = CosmicOnSurface
                    )
                }
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CosmicSecondary,
                        checkedTrackColor = CosmicSecondary.copy(alpha = 0.35f),
                        uncheckedThumbColor = CosmicOnSurfaceVariant,
                        uncheckedTrackColor = CosmicSurfaceContainerHigh
                    )
                )
            }

            // Save Action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(CosmicSecondaryContainer, CosmicPrimaryContainer)
                        )
                    )
                    .clickable {
                        viewModel.saveNotes(dream!!.id, notesText, isFavorite)
                    }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SAVE NOTES & BOOKMARK",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ShareExportDialog(
    viewModel: DreamViewModel,
    onDismiss: () -> Unit
) {
    val dream by viewModel.selectedDream.collectAsState()
    if (dream == null) return
    val context = LocalContext.current
    val currentDream = dream!!

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicTertiary.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Share & Export Dream",
                    fontFamily = PlayfairFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CosmicOnSurface
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CosmicOnSurfaceVariant
                    )
                }
            }

            ExportOptionRow(
                icon = Icons.Default.PictureAsPdf,
                title = "Generate PDF Report",
                tint = AuroraPink,
                onClick = {
                    val reportText = """
                        ✨ DREAMSCAPE NOCTURNAL REPORT ✨
                        Title: ${currentDream.title}
                        Date: ${currentDream.date} at ${currentDream.time}
                        Archetype: ${currentDream.archetype}
                        Lucidity: ${currentDream.lucidityLevel} (${currentDream.lucidityPercent}%)
                        Coherence: ${currentDream.coherencePercent}% | Intensity: ${currentDream.emotionIntensityPercent}%
                        Mood: ${currentDream.mood}
                        
                        --- Subconscious Narrative ---
                        ${currentDream.narrative}
                        
                        --- Jungian Oneiric Interpretation ---
                        ${currentDream.interpretation}
                        
                        Personal Notes: ${currentDream.personalNotes}
                        Tags: ${currentDream.tags.joinToString(", ")}
                    """.trimIndent()

                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Dreamscape PDF Report", reportText))

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, reportText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Export Dream Report"))
                    onDismiss()
                    viewModel.showToast("Dream report generated and copied! 📄✨")
                }
            )

            ExportOptionRow(
                icon = Icons.Default.TextSnippet,
                title = "Export as Text",
                tint = CosmicPrimary,
                onClick = {
                    val textToCopy = "\"${currentDream.title}\"\n\n${currentDream.narrative}\n\n— Captured on Dreamscape AI"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Dream Transcript", textToCopy))

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, textToCopy)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Dream Narrative"))
                    onDismiss()
                    viewModel.showToast("Transcript copied to clipboard & ready to share!")
                }
            )

            ExportOptionRow(
                icon = Icons.Default.Image,
                title = "Share Celestial Canvas",
                tint = CosmicTertiary,
                onClick = {
                    val shareMsg = "✨ Celestial Dream Canvas for \"${currentDream.title}\"\n\n${currentDream.narrative}\n\nView canvas: ${currentDream.imageUrl}"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareMsg)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Dream Canvas"))
                    onDismiss()
                    viewModel.showToast("Opening share sheet for artwork...")
                }
            )

            ExportOptionRow(
                icon = Icons.Default.ContentCopy,
                title = "Copy Dream Link",
                tint = CosmicSecondary,
                onClick = {
                    val link = "https://dreamscape.app/dream/${currentDream.id}"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Dream Link", link))
                    onDismiss()
                    viewModel.showToast("Link copied: $link")
                }
            )

            ExportOptionRow(
                icon = Icons.Default.CloudUpload,
                title = "Backup to Google Drive / Cloud",
                tint = CosmicPrimary,
                onClick = {
                    val backupPayload = "DREAMSCAPE CLOUD BACKUP\nID: ${currentDream.id}\nTitle: ${currentDream.title}\nDate: ${currentDream.date}\nNarrative: ${currentDream.narrative}\nNotes: ${currentDream.personalNotes}"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, backupPayload)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Save to Google Drive / Cloud"))
                    onDismiss()
                    viewModel.showToast("Enshrining backup safely to cloud")
                }
            )
        }
    }
}

@Composable
fun ExportOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CosmicSurfaceContainerLow)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = title,
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = CosmicOnSurface
        )
    }
}

@Composable
fun DreamMapDialog(
    viewModel: DreamViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicTertiary.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        imageVector = Icons.Default.Hub,
                        contentDescription = null,
                        tint = CosmicTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Subconscious Dream Map",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CosmicOnSurface
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CosmicOnSurfaceVariant
                    )
                }
            }

            Text(
                text = "Topography & nodes connecting astral portals across recurring memory clusters.",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = CosmicOnSurfaceVariant
            )

            // Constellation node map visualization
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CosmicSurfaceContainerLow)
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MapNodeItem(name = "Floating Citadels", tag = "Lucid Node #1", color = CosmicPrimary)
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .width(2.dp)
                        .height(20.dp)
                        .background(CosmicTertiary.copy(alpha = 0.4f))
                )
                MapNodeItem(name = "Moonlit Shoreline", tag = "Serene Portal #2", color = CosmicTertiary)
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .width(2.dp)
                        .height(20.dp)
                        .background(CosmicSecondary.copy(alpha = 0.4f))
                )
                MapNodeItem(name = "Crystal Amethyst Caverns", tag = "High Harmonic Node #3", color = CosmicSecondary)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(CosmicPrimaryContainer)
                    .clickable {
                        onDismiss()
                        viewModel.showToast("Navigating to Floating Citadels cluster")
                    }
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTER ASTRAL TOPOGRAPHY",
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MapNodeItem(name: String, tag: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(
                text = name,
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = CosmicOnSurface
            )
            Text(
                text = tag,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                color = color
            )
        }
    }
}

@Composable
fun AddTagDialog(
    viewModel: DreamViewModel,
    onDismiss: () -> Unit
) {
    val dream by viewModel.selectedDream.collectAsState()
    if (dream == null) return

    var newTagText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CosmicSurfaceContainerLowest)
                .border(1.dp, CosmicPrimary.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Add Dream Tag",
                fontFamily = PlayfairFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CosmicOnSurface
            )
            Text(
                text = "Enter a keyword, archetype, or emotional symbol.",
                fontFamily = PlusJakartaFontFamily,
                fontSize = 12.sp,
                color = CosmicOnSurfaceVariant
            )

            OutlinedTextField(
                value = newTagText,
                onValueChange = { newTagText = it },
                label = { Text("e.g. Lucid Wonder") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CosmicPrimary,
                    unfocusedBorderColor = CosmicSurfaceContainerHighest,
                    focusedTextColor = CosmicOnSurface,
                    unfocusedTextColor = CosmicOnSurface
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(CosmicSurfaceContainerHigh)
                        .clickable { onDismiss() }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Cancel", fontSize = 12.sp, color = CosmicOnSurfaceVariant)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(CosmicPrimary)
                        .clickable {
                            if (newTagText.isNotBlank()) {
                                viewModel.addTag(dream!!.id, newTagText.trim())
                                onDismiss()
                            }
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Add Tag", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CosmicBackground)
                }
            }
        }
    }
}
