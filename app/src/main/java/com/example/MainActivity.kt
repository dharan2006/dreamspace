package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AmbientAuraBackground
import com.example.ui.components.FloatingGlassDock
import com.example.ui.components.FloatingToast
import com.example.ui.screens.AddTagDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookmarkNotesDialog
import com.example.ui.screens.CaptureScreen
import com.example.ui.screens.DreamDetailScreen
import com.example.ui.screens.DreamLibraryScreen
import com.example.ui.screens.DreamMapDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImageVariationsDialog
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.ProcessingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ShareExportDialog
import com.example.ui.screens.attemptAutoSignIn
import com.example.ui.theme.DreamscapeTheme
import com.example.data.firebase.configureAppCheck
import com.example.viewmodel.AppTab
import com.example.viewmodel.DreamViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            DreamscapeTheme {
                MainRoot()
            }
        }
    }
}

@Composable
fun MainRoot() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    LaunchedEffect(Unit) {
        if (currentUser == null) {
            attemptAutoSignIn(
                context = context,
                credentialManager = credentialManager,
                onAuthSuccess = {
                    currentUser = Firebase.auth.currentUser
                },
                onUnauthenticated = {},
                scope = coroutineScope
            )
        }
    }

    if (currentUser == null) {
        AuthScreen(
            onAuthSuccess = {
                currentUser = Firebase.auth.currentUser
            }
        )
    } else {
        val viewModel: DreamViewModel = viewModel()
        LaunchedEffect(currentUser?.uid) {
            viewModel.initFirestore(context)
        }
        DreamscapeApp(viewModel = viewModel)
    }
}

@Composable
fun DreamscapeApp(
    viewModel: DreamViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isCaptureActive by viewModel.isCaptureActive.collectAsState()
    val isProcessingActive by viewModel.isProcessingActive.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val showImageVariations by viewModel.showImageVariations.collectAsState()
    val showBookmarkNotes by viewModel.showBookmarkNotes.collectAsState()
    val showShareExport by viewModel.showShareExport.collectAsState()
    val showDreamMap by viewModel.showDreamMap.collectAsState()
    val showAddTagDialog by viewModel.showAddTagDialog.collectAsState()

    var activeDetailDreamId by remember { mutableStateOf<String?>(null) }

    // Handle back button smoothly
    BackHandler(enabled = isProcessingActive || isCaptureActive || activeDetailDreamId != null || currentTab != AppTab.HOME) {
        when {
            isProcessingActive -> {
                viewModel.closeCaptureScreen()
            }
            isCaptureActive -> {
                viewModel.closeCaptureScreen()
            }
            activeDetailDreamId != null -> {
                activeDetailDreamId = null
            }
            currentTab != AppTab.HOME -> {
                viewModel.selectTab(AppTab.HOME)
            }
        }
    }

    AmbientAuraBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isProcessingActive -> {
                    ProcessingScreen(viewModel = viewModel)
                }
                isCaptureActive -> {
                    CaptureScreen(viewModel = viewModel)
                }
                activeDetailDreamId != null -> {
                    DreamDetailScreen(
                        viewModel = viewModel,
                        onBack = { activeDetailDreamId = null }
                    )
                }
                else -> {
                    // Main Tabs
                    when (currentTab) {
                        AppTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppTab.LIBRARY -> {
                            DreamLibraryScreen(
                                viewModel = viewModel,
                                onDreamClick = { dreamId ->
                                    viewModel.openDreamDetail(dreamId)
                                    activeDetailDreamId = dreamId
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppTab.INSIGHTS -> {
                            InsightsScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppTab.PROFILE -> {
                            ProfileScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Floating Bottom Glass Dock
                    FloatingGlassDock(
                        currentTab = currentTab,
                        onTabSelected = { tab ->
                            activeDetailDreamId = null
                            viewModel.selectTab(tab)
                        },
                        onCaptureClick = {
                            activeDetailDreamId = null
                            viewModel.openCaptureScreen()
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            // Top Floating Toast Notification
            FloatingToast(
                message = toastMessage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
            )

            // Modals & Dialogs
            if (showImageVariations) {
                ImageVariationsDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showImageVariations.value = false }
                )
            }

            if (showBookmarkNotes) {
                BookmarkNotesDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showBookmarkNotes.value = false }
                )
            }

            if (showShareExport) {
                ShareExportDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showShareExport.value = false }
                )
            }

            if (showDreamMap) {
                DreamMapDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showDreamMap.value = false }
                )
            }

            if (showAddTagDialog) {
                AddTagDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.showAddTagDialog.value = false }
                )
            }
        }
    }
}
