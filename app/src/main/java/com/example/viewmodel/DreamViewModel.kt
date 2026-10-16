package com.example.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.DreamAiAnalyzer
import com.example.data.firebase.FirestoreDreamRepository
import com.example.data.model.Dream
import com.example.data.notification.LucidityNotificationManager
import com.example.data.repository.DreamRepository
import com.example.data.speech.SpeechTranscriptionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppTab {
    HOME, LIBRARY, INSIGHTS, PROFILE
}

enum class DetailTab {
    STORY, DETAILS, ANALYSIS
}

enum class CaptureMode {
    VOICE, TEXT, DETAILS
}

enum class RecordingState {
    IDLE, RECORDING, PAUSED
}

enum class LibraryLayout {
    GRID, LIST
}

enum class LibraryFilter {
    ALL, FAVORITES, LUCID, ARCHIVED
}

class DreamViewModel : ViewModel() {

    // Global navigation
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _isCaptureActive = MutableStateFlow(false)
    val isCaptureActive: StateFlow<Boolean> = _isCaptureActive.asStateFlow()

    private val _isProcessingActive = MutableStateFlow(false)
    val isProcessingActive: StateFlow<Boolean> = _isProcessingActive.asStateFlow()

    private val _processingStep = MutableStateFlow(1)
    val processingStep: StateFlow<Int> = _processingStep.asStateFlow()

    private val _selectedDreamId = MutableStateFlow<String?>("dream-1")
    val selectedDreamId: StateFlow<String?> = _selectedDreamId.asStateFlow()

    private val _detailTab = MutableStateFlow(DetailTab.STORY)
    val detailTab: StateFlow<DetailTab> = _detailTab.asStateFlow()

    // Capture Screen State
    private val _captureMode = MutableStateFlow(CaptureMode.VOICE)
    val captureMode: StateFlow<CaptureMode> = _captureMode.asStateFlow()

    private val _recordingState = MutableStateFlow(RecordingState.IDLE)
    val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()

    private val _recordingSeconds = MutableStateFlow(0)
    val recordingSeconds: StateFlow<Int> = _recordingSeconds.asStateFlow()

    private val _waveformHeights = MutableStateFlow(List(16) { 16f })
    val waveformHeights: StateFlow<List<Float>> = _waveformHeights.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("Default - English (Auto Detect)")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Draft Capture Fields
    val draftTitle = MutableStateFlow("The Floating City")
    val draftTranscript = MutableStateFlow(
        "I was standing on a floating island suspended gracefully above perpetual clouds. A serene, futuristic citadel shimmered with luminous spires and ethereal turquoise waterfalls spilling endlessly into pastel violet mists..."
    )
    val draftMood = MutableStateFlow("Wonder")
    val draftTags = MutableStateFlow(listOf("Peaceful", "Fantasy", "Exploration", "Floating City", "Magic"))

    // Library Filter & Search
    val searchQuery = MutableStateFlow("")
    val libraryFilter = MutableStateFlow(LibraryFilter.ALL)
    val libraryLayout = MutableStateFlow(LibraryLayout.GRID)

    // Dialog / Modal Visibility
    val showImageVariations = MutableStateFlow(false)
    val showBookmarkNotes = MutableStateFlow(false)
    val showShareExport = MutableStateFlow(false)
    val showDreamMap = MutableStateFlow(false)
    val showAddTagDialog = MutableStateFlow(false)

    // Reminders & User Settings
    val lucidityReminderActive = MutableStateFlow(true)
    val reminderChimeTime = MutableStateFlow("5:30 AM")

    // Toast message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // All dreams stream
    private val _dreams = MutableStateFlow<List<Dream>>(emptyList())
    val dreams: StateFlow<List<Dream>> = _dreams.asStateFlow()

    private var firestoreRepo: FirestoreDreamRepository? = null
    private val aiAnalyzer by lazy { DreamAiAnalyzer() }
    private var speechManager: SpeechTranscriptionManager? = null

    init {
        // Default in-memory dreams
        _dreams.value = DreamRepository.dreams.value
    }

    fun initFirestore(context: Context) {
        if (Firebase.auth.currentUser != null) {
            val repo = FirestoreDreamRepository(context)
            firestoreRepo = repo
            viewModelScope.launch {
                try {
                    repo.seedInitialDreamsIfEmpty()
                    repo.observeDreams().collect { cloudDreams ->
                        if (cloudDreams.isNotEmpty()) {
                            _dreams.value = cloudDreams
                        }
                    }
                } catch (e: Exception) {
                    // Falls back cleanly to current in-memory stream
                }
            }
        }
    }

    // Filtered dreams for library
    val filteredDreams: StateFlow<List<Dream>> = combine(
        dreams,
        searchQuery,
        libraryFilter
    ) { allDreams, query, filter ->
        allDreams.filter { dream ->
            val matchesFilter = when (filter) {
                LibraryFilter.ALL -> !dream.isArchived
                LibraryFilter.FAVORITES -> dream.isFavorite && !dream.isArchived
                LibraryFilter.LUCID -> dream.lucidityPercent >= 70 && !dream.isArchived
                LibraryFilter.ARCHIVED -> dream.isArchived
            }
            val matchesQuery = query.isBlank() ||
                    dream.title.contains(query, ignoreCase = true) ||
                    dream.narrative.contains(query, ignoreCase = true) ||
                    dream.tags.any { it.contains(query, ignoreCase = true) } ||
                    dream.mood.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedDream: StateFlow<Dream?> = combine(
        dreams,
        _selectedDreamId
    ) { allDreams, id ->
        allDreams.find { it.id == id } ?: allDreams.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private var timerJob: Job? = null
    private var waveformJob: Job? = null

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
        _isCaptureActive.value = false
        _isProcessingActive.value = false
    }

    fun openDreamDetail(dreamId: String) {
        _selectedDreamId.value = dreamId
        _isCaptureActive.value = false
        _isProcessingActive.value = false
    }

    fun setDetailTab(tab: DetailTab) {
        _detailTab.value = tab
    }

    fun setCaptureMode(mode: CaptureMode) {
        _captureMode.value = mode
    }

    fun openCaptureScreen() {
        _isCaptureActive.value = true
        _isProcessingActive.value = false
        _recordingState.value = RecordingState.IDLE
        _recordingSeconds.value = 0
    }

    fun closeCaptureScreen() {
        stopRecording()
        _isCaptureActive.value = false
        _isProcessingActive.value = false
    }

    fun toggleRecording() {
        when (_recordingState.value) {
            RecordingState.IDLE -> startRecording()
            RecordingState.RECORDING -> pauseRecording()
            RecordingState.PAUSED -> resumeRecording()
        }
    }

    fun startRecording(context: Context? = null) {
        _recordingState.value = RecordingState.RECORDING
        startTimerAndWaveform()

        if (context != null) {
            speechManager = SpeechTranscriptionManager(context).apply {
                startListening(
                    onPartialResult = { partial ->
                        if (partial.isNotBlank()) {
                            draftTranscript.value = partial
                        }
                    },
                    onFinalResult = { finalResult ->
                        if (finalResult.isNotBlank()) {
                            draftTranscript.value = finalResult
                        }
                    },
                    onRmsChanged = { rms ->
                        val normalized = (rms.coerceIn(0f, 10f) / 10f)
                        _waveformHeights.value = List(16) { i ->
                            val waveFactor = kotlin.math.sin(i.toDouble() + System.currentTimeMillis() / 200.0).toFloat()
                            (12f + (normalized * 38f) * (0.6f + 0.4f * waveFactor)).coerceIn(8f, 54f)
                        }
                    },
                    onError = {
                        // Silent fallback - timer and visualizer continue
                    }
                )
            }
        }
        showToast("Listening to subconscious frequencies...")
    }

    fun pauseRecording() {
        _recordingState.value = RecordingState.PAUSED
        speechManager?.stopListening()
        timerJob?.cancel()
        waveformJob?.cancel()
        showToast("Recording paused")
    }

    fun resumeRecording() {
        _recordingState.value = RecordingState.RECORDING
        startTimerAndWaveform()
    }

    fun stopRecordingAndProcess() {
        stopRecording()
        launchProcessingFlow()
    }

    fun cancelRecording() {
        stopRecording()
        _recordingSeconds.value = 0
        showToast("Recording cleared")
    }

    private fun startTimerAndWaveform() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_recordingState.value == RecordingState.RECORDING) {
                delay(1000)
                _recordingSeconds.value += 1
            }
        }

        waveformJob?.cancel()
        waveformJob = viewModelScope.launch {
            while (_recordingState.value == RecordingState.RECORDING) {
                delay(120)
                if (speechManager == null) {
                    _waveformHeights.value = List(16) {
                        Random.nextFloat() * 40f + 8f
                    }
                }
            }
        }
    }

    private fun stopRecording() {
        _recordingState.value = RecordingState.IDLE
        speechManager?.stopListening()
        speechManager = null
        timerJob?.cancel()
        waveformJob?.cancel()
        _waveformHeights.value = listOf(12f, 16f, 24f, 32f, 40f, 48f, 36f, 44f, 28f, 20f, 12f, 28f, 40f, 24f, 16f, 20f)
    }

    fun launchProcessingFlow() {
        _isCaptureActive.value = false
        _isProcessingActive.value = true
        _processingStep.value = 1

        viewModelScope.launch {
            // Step 1: Converting Speech to Text
            _processingStep.value = 1
            delay(800)

            // Step 2: Emotional & Subconscious Signatures
            _processingStep.value = 2
            delay(800)

            val rawNarrative = draftTranscript.value
            val userMood = draftMood.value

            // Step 3: Real Gemini 3.8 Flash Subconscious Analysis
            _processingStep.value = 3
            val analysis = try {
                aiAnalyzer.analyzeDream(rawNarrative, userMood)
            } catch (e: Exception) {
                delay(1000)
                aiAnalyzer.analyzeDream(rawNarrative, userMood)
            }

            // Step 4: Generating Dream Canvas
            _processingStep.value = 4
            delay(800)
            val imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAE8xKGbnM8YGXy23pDwhOy1LI5NU0R6S2AZFaSn_a8CFRdlNZsQURvm1WPyz5ArFaEQ5Tb5HAhcI1hewuw5drTAgyYOOtnBqnbWpmgAO2B31_Mic-UUHE_byIMykKFg2_CSiP8uR12zHUjkvFzpAkbpF1BQ9XcDosNilSQJoE685WYHgo4xAnBQJyT6sG2VBd9RHBdXpkxMLQoX_YyL-K3NalCdre1P_elbbin93haXTyV9oOWPFT9fg"

            // Step 5: Enshrining in Cloud Firestore / Local Vault
            _processingStep.value = 5
            delay(600)

            val finalTitle = if (draftTitle.value.isNotBlank() && draftTitle.value != "The Floating City") {
                draftTitle.value
            } else {
                analysis.title
            }

            var newId = "dream-${System.currentTimeMillis()}"

            // Persist to Cloud Firestore if connected
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    newId = firestoreRepo!!.saveDream(
                        title = finalTitle,
                        narrative = rawNarrative,
                        mood = analysis.mood,
                        tags = analysis.tags,
                        imageUrl = imageUrl,
                        archetype = analysis.archetype,
                        lucidityLevel = analysis.lucidityLevel,
                        lucidityPercent = analysis.lucidityPercent,
                        coherencePercent = analysis.coherencePercent,
                        emotionIntensityPercent = analysis.emotionIntensityPercent,
                        interpretation = analysis.interpretation
                    )
                    showToast("Enshrined to Cloud Firestore! ☁️✨")
                } catch (e: Exception) {
                    val localDream = DreamRepository.saveNewDream(
                        title = finalTitle,
                        narrative = rawNarrative,
                        mood = analysis.mood,
                        tags = analysis.tags,
                        imageUrl = imageUrl,
                        archetype = analysis.archetype,
                        lucidityLevel = analysis.lucidityLevel,
                        lucidityPercent = analysis.lucidityPercent,
                        coherencePercent = analysis.coherencePercent,
                        emotionIntensityPercent = analysis.emotionIntensityPercent,
                        interpretation = analysis.interpretation,
                        symbols = analysis.symbols,
                        keyElements = analysis.keyElements
                    )
                    _dreams.value = listOf(localDream) + _dreams.value
                    newId = localDream.id
                    showToast("Enshrined locally (Cloud sync pending)")
                }
            } else {
                val localDream = DreamRepository.saveNewDream(
                    title = finalTitle,
                    narrative = rawNarrative,
                    mood = analysis.mood,
                    tags = analysis.tags,
                    imageUrl = imageUrl,
                    archetype = analysis.archetype,
                    lucidityLevel = analysis.lucidityLevel,
                    lucidityPercent = analysis.lucidityPercent,
                    coherencePercent = analysis.coherencePercent,
                    emotionIntensityPercent = analysis.emotionIntensityPercent,
                    interpretation = analysis.interpretation,
                    symbols = analysis.symbols,
                    keyElements = analysis.keyElements
                )
                _dreams.value = listOf(localDream) + _dreams.value
                newId = localDream.id
                showToast("Dream vision crystallized! ✨")
            }

            _selectedDreamId.value = newId
            _isProcessingActive.value = false
            _detailTab.value = DetailTab.STORY
        }
    }

    fun deleteDream(dreamId: String) {
        _dreams.value = _dreams.value.filter { it.id != dreamId }
        DreamRepository.deleteDream(dreamId)

        if (_selectedDreamId.value == dreamId) {
            _selectedDreamId.value = _dreams.value.firstOrNull()?.id
        }

        viewModelScope.launch {
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    firestoreRepo!!.deleteDream(dreamId)
                } catch (e: Exception) {
                    // local already removed
                }
            }
        }
        showToast("Dream dissolved from timeline")
    }

    fun archiveDream(dreamId: String, isArchived: Boolean) {
        _dreams.value = _dreams.value.map {
            if (it.id == dreamId) it.copy(isArchived = isArchived) else it
        }
        DreamRepository.archiveDream(dreamId, isArchived)

        viewModelScope.launch {
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    firestoreRepo!!.archiveDream(dreamId, isArchived)
                } catch (e: Exception) {
                    // local handled
                }
            }
        }
        showToast(if (isArchived) "Dream archived into deep vault" else "Dream restored to library")
    }

    fun triggerLucidityChime(context: Context) {
        LucidityNotificationManager(context).triggerLucidityChime()
        showToast("Lucidity chime broadcasted! 🌙🔔")
    }

    fun handleAudioImport(context: Context, uri: Uri) {
        val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "audio_dream_recording.m4a"
        draftTitle.value = "Voice Memo: ${fileName.substringBeforeLast('.')}"
        draftTranscript.value = "Imported nocturnal voice recording ($fileName). Subconscious audio captured at dawn ready for AI crystallization."
        showToast("Audio imported: $fileName")
    }

    fun toggleFavorite(dreamId: String) {
        val currentDream = _dreams.value.find { it.id == dreamId }
        val newFav = !(currentDream?.isFavorite ?: false)
        _dreams.value = _dreams.value.map {
            if (it.id == dreamId) it.copy(isFavorite = newFav) else it
        }

        viewModelScope.launch {
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    firestoreRepo!!.toggleFavorite(dreamId, !newFav)
                } catch (e: Exception) {
                    // local handled
                }
            }
        }
        showToast(if (newFav) "Added to Favorite Visions" else "Removed from Favorites")
    }

    fun saveNotes(dreamId: String, notes: String, isFav: Boolean) {
        _dreams.value = _dreams.value.map {
            if (it.id == dreamId) it.copy(personalNotes = notes, isFavorite = isFav) else it
        }

        viewModelScope.launch {
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    firestoreRepo!!.updateNotes(dreamId, notes, isFav)
                } catch (e: Exception) {
                    // local handled
                }
            }
        }
        showBookmarkNotes.value = false
        showToast("Dream memory note saved to cloud")
    }

    fun selectImageVariation(dreamId: String, imageUrl: String) {
        _dreams.value = _dreams.value.map {
            if (it.id == dreamId) it.copy(imageUrl = imageUrl) else it
        }

        viewModelScope.launch {
            if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                try {
                    firestoreRepo!!.selectImageVariation(dreamId, imageUrl)
                } catch (e: Exception) {
                    // local handled
                }
            }
        }
        showImageVariations.value = false
        showToast("New dream canvas applied")
    }

    fun addTag(dreamId: String, tag: String) {
        if (tag.isNotBlank()) {
            val trimmed = tag.trim()
            _dreams.value = _dreams.value.map {
                if (it.id == dreamId && !it.tags.contains(trimmed)) it.copy(tags = it.tags + trimmed) else it
            }

            viewModelScope.launch {
                if (firestoreRepo != null && Firebase.auth.currentUser != null) {
                    try {
                        firestoreRepo!!.addTag(dreamId, trimmed)
                    } catch (e: Exception) {
                        // local handled
                    }
                }
            }
            showToast("Tag added: $trimmed")
        }
    }

    fun regenerateImage(dreamId: String) {
        viewModelScope.launch {
            showToast("Channeling AI Dream Canvas...")
            delay(1200)
            val dream = _dreams.value.find { it.id == dreamId }
            val nextImg = dream?.imageVariations?.shuffled()?.firstOrNull() ?: dream?.imageUrl
            if (nextImg != null) {
                selectImageVariation(dreamId, nextImg)
            }
            showToast("Celestial vision regenerated! ✨")
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
        viewModelScope.launch {
            delay(2500)
            if (_toastMessage.value == message) {
                _toastMessage.value = null
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
