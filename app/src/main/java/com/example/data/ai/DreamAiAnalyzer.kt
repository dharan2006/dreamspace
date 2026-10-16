package com.example.data.ai

import android.util.Log
import com.example.data.model.DreamKeyElements
import com.example.data.model.SubconsciousSymbol
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "DreamAiAnalyzer"

data class DreamAnalysisResult(
    val title: String,
    val archetype: String,
    val lucidityLevel: String,
    val lucidityPercent: Int,
    val coherencePercent: Int,
    val emotionIntensityPercent: Int,
    val mood: String,
    val interpretation: String,
    val symbols: List<SubconsciousSymbol>,
    val tags: List<String>,
    val keyElements: DreamKeyElements
)

class DreamAiAnalyzer {

    private val analysisSchema = Schema.obj(
        properties = mapOf(
            "celestialTitle" to Schema.string("An evocative poetic celestial title for the dream"),
            "archetype" to Schema.string("The primary psychological or Jungian dream archetype"),
            "lucidityLevel" to Schema.string("e.g. Lucid Level I, Lucid Level II, Deep Somnium"),
            "lucidityPercent" to Schema.integer("Lucidity percentage between 10 and 100"),
            "coherencePercent" to Schema.integer("Narrative coherence percentage between 10 and 100"),
            "emotionIntensityPercent" to Schema.integer("Emotional intensity percentage between 10 and 100"),
            "mood" to Schema.string("Primary dream emotional mood"),
            "interpretation" to Schema.string("Deep psychological and subconscious interpretation of the dream"),
            "tags" to Schema.array(Schema.string("Concise dream tag")),
            "symbol1Name" to Schema.string("First key symbol name"),
            "symbol1Meaning" to Schema.string("First key symbol subconscious meaning"),
            "symbol2Name" to Schema.string("Second key symbol name"),
            "symbol2Meaning" to Schema.string("Second key symbol subconscious meaning"),
            "characters" to Schema.string("Main entities or characters observed"),
            "locations" to Schema.string("Primary dream location description"),
            "objects" to Schema.string("Key objects or artifacts encountered"),
            "sensoryVisual" to Schema.string("Vivid visual texture observed"),
            "sensorySound" to Schema.string("Auditory landscape experienced"),
            "sensoryFeeling" to Schema.string("Somatic or emotional sensations")
        )
    )

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = analysisSchema
            }
        )
    }

    suspend fun analyzeDream(narrative: String, userMoodHint: String): DreamAnalysisResult {
        return try {
            withTimeout(45_000L) {
                val prompt = """
                    You are Dreamscape's advanced nocturnal AI psychotherapist and oneiric analyzer.
                    Analyze this dream narrative with celestial depth and Jungian subconscious analysis:
                    
                    Dream Narrative: "$narrative"
                    User Mood Hint: "$userMoodHint"
                    
                    Provide accurate structured JSON matching the schema with poetic, insightful interpretations.
                """.trimIndent()

                val response = model.generateContent(prompt)
                val rawJson = response.text.orEmpty()
                parseAnalysisJson(rawJson, narrative, userMoodHint)
            }
        } catch (e: TimeoutCancellationException) {
            Log.w(TAG, "AI dream analysis timed out after 45s, using heuristic synthesis", e)
            buildHeuristicAnalysis(narrative, userMoodHint)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini dream analysis failed, using heuristic synthesis: ${e.message}", e)
            buildHeuristicAnalysis(narrative, userMoodHint)
        }
    }

    private fun parseAnalysisJson(rawJson: String, originalNarrative: String, userMoodHint: String): DreamAnalysisResult {
        return try {
            val json = JSONObject(rawJson)
            val title = json.optString("celestialTitle").ifBlank { "The Ethereal Horizon" }
            val archetype = json.optString("archetype").ifBlank { "The Cosmic Traveler" }
            val lucidityLevel = json.optString("lucidityLevel").ifBlank { "Lucid Level II" }
            val lucidityPercent = json.optInt("lucidityPercent", 85).coerceIn(10, 100)
            val coherencePercent = json.optInt("coherencePercent", 90).coerceIn(10, 100)
            val emotionIntensityPercent = json.optInt("emotionIntensityPercent", 80).coerceIn(10, 100)
            val mood = json.optString("mood").ifBlank { userMoodHint.ifBlank { "Wonder" } }
            val interpretation = json.optString("interpretation").ifBlank {
                "Your subconscious is synthesizing emotional clarity, exploring uncharted realms of inner wisdom."
            }

            val tagsList = mutableListOf<String>()
            val tagsArray = json.optJSONArray("tags")
            if (tagsArray != null) {
                for (i in 0 until tagsArray.length()) {
                    val t = tagsArray.optString(i)
                    if (t.isNotBlank()) tagsList.add(t)
                }
            }
            if (tagsList.isEmpty()) {
                tagsList.addAll(listOf("Lucid", mood, "Subconscious", "Vision"))
            }

            val sym1Name = json.optString("symbol1Name").ifBlank { "The Portal" }
            val sym1Meaning = json.optString("symbol1Meaning").ifBlank { "Represents a transition between conscious intent and hidden desires." }
            val sym2Name = json.optString("symbol2Name").ifBlank { "The Celestial Mist" }
            val sym2Meaning = json.optString("symbol2Meaning").ifBlank { "Signifies mysteries waiting to be unveiled in waking life." }

            val symbols = listOf(
                SubconsciousSymbol(sym1Name, sym1Meaning, "auto_awesome"),
                SubconsciousSymbol(sym2Name, sym2Meaning, "nights_stay")
            )

            val characters = json.optString("characters").ifBlank { "Inner Guide / Dreamer Presence" }
            val locations = json.optString("locations").ifBlank { "Suspended Astral Realm" }
            val objects = json.optString("objects").ifBlank { "Luminous Artifacts" }
            val sensoryVisual = json.optString("sensoryVisual").ifBlank { "Iridescent cyan cascades and violet nebulae" }
            val sensorySound = json.optString("sensorySound").ifBlank { "Deep resonant frequency and whispering wind" }
            val sensoryFeeling = json.optString("sensoryFeeling").ifBlank { "Weightless equilibrium and serene calm" }

            DreamAnalysisResult(
                title = title,
                archetype = archetype,
                lucidityLevel = lucidityLevel,
                lucidityPercent = lucidityPercent,
                coherencePercent = coherencePercent,
                emotionIntensityPercent = emotionIntensityPercent,
                mood = mood,
                interpretation = interpretation,
                symbols = symbols,
                tags = tagsList.take(6),
                keyElements = DreamKeyElements(
                    mainCharacters = characters,
                    locations = locations,
                    objects = objects,
                    themes = "Transcendence, Exploration",
                    sensoryVisual = sensoryVisual,
                    sensorySound = sensorySound,
                    sensoryFeeling = sensoryFeeling
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed parsing JSON output: $rawJson", e)
            buildHeuristicAnalysis(originalNarrative, userMoodHint)
        }
    }

    private fun buildHeuristicAnalysis(narrative: String, userMoodHint: String): DreamAnalysisResult {
        val words = narrative.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val preview = words.take(4).joinToString(" ").replace("[^a-zA-Z0-9 ]".toRegex(), "")
        val title = if (preview.length > 5) "The $preview" else "The Celestial Sanctuary"

        val mood = if (userMoodHint.isNotBlank()) userMoodHint else "Wonder"

        return DreamAnalysisResult(
            title = title,
            archetype = "The Cosmic Traveler",
            lucidityLevel = "Lucid Level II",
            lucidityPercent = 86,
            coherencePercent = 91,
            emotionIntensityPercent = 82,
            mood = mood,
            interpretation = "An exceptionally lucid somnium reflecting conscious awareness merging with nocturnal creativity. You are processing pivotal subconscious aspirations with tranquility.",
            symbols = listOf(
                SubconsciousSymbol("The Celestial Realm", "Represents ascension and transcendence above daily trivialities.", "auto_awesome"),
                SubconsciousSymbol("The Luminescent Horizon", "Symbolizes forward progress and awakened psychic clarity.", "visibility")
            ),
            tags = listOf("Astral", mood, "Lucid", "Ethereal", "Clarity"),
            keyElements = DreamKeyElements(
                mainCharacters = "The Dreamer / Guiding Consciousness",
                locations = "Luminous Celestial Haven",
                objects = "Prismatic Starlight",
                themes = "Astral Elevation, Awakening",
                sensoryVisual = "Vibrant violet aura with glowing turquoise cascades",
                sensorySound = "Harmonic ethereal chimes vibrating softly",
                sensoryFeeling = "Deep tranquility and floating weightlessness"
            )
        )
    }
}
