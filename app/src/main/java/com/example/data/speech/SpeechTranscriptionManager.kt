package com.example.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

private const val TAG = "SpeechManager"

class SpeechTranscriptionManager(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    fun startListening(
        onPartialResult: (String) -> Unit,
        onFinalResult: (String) -> Unit,
        onRmsChanged: (Float) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isAvailable()) {
            onError("Speech recognition service is not available on this device.")
            return
        }

        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                        Log.d(TAG, "SpeechRecognizer: Ready for nocturnal voice capture")
                    }

                    override fun onBeginningOfSpeech() {}

                    override fun onRmsChanged(rmsdB: Float) {
                        onRmsChanged(rmsdB)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        isListening = false
                    }

                    override fun onError(error: Int) {
                        isListening = false
                        val errorMsg = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                            SpeechRecognizer.ERROR_CLIENT -> "Client speech error"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Audio permission required"
                            SpeechRecognizer.ERROR_NETWORK -> "Network required for speech recognition"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Speech recognition network timeout"
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Speak clearly into the microphone."
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy, please retry"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                            else -> "Speech recognition paused (code $error)"
                        }
                        Log.w(TAG, "Speech error: $errorMsg ($error)")
                        onError(errorMsg)
                    }

                    override fun onResults(results: Bundle?) {
                        isListening = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull().orEmpty()
                        if (text.isNotBlank()) {
                            onFinalResult(text)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull().orEmpty()
                        if (text.isNotBlank()) {
                            onPartialResult(text)
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed starting speech recognizer", e)
            onError("Could not initialize microphone: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            if (isListening || speechRecognizer != null) {
                speechRecognizer?.stopListening()
                speechRecognizer?.destroy()
                speechRecognizer = null
                isListening = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping speech recognizer", e)
        }
    }
}
