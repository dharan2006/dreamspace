package com.example.data.firebase

import com.example.data.model.Dream
import com.example.data.model.DreamKeyElements
import com.example.data.model.SubconsciousSymbol
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreDream(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val narrative: String = "",
    val expandedNarrative: String = "",
    val date: String = "",
    val time: String = "",
    val mood: String = "Wonder",
    val category: String = "Fantasy Realm",
    val lucidityLevel: String = "Lucid Level II",
    val lucidityPercent: Int = 85,
    val coherencePercent: Int = 92,
    val emotionIntensityPercent: Int = 80,
    val imageUrl: String = "",
    val imageVariations: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val personalNotes: String = "",
    val interpretation: String = "",
    val estimatedRemTime: String = "42 min",
    val clarityScore: String = "8.9 / 10",
    val archetype: String = "The Sage Archetype",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toDomainDream(): Dream {
        return Dream(
            id = id,
            title = title,
            date = date,
            time = time,
            lucidityLevel = lucidityLevel,
            lucidityPercent = lucidityPercent,
            coherencePercent = coherencePercent,
            emotionIntensityPercent = emotionIntensityPercent,
            imageUrl = imageUrl,
            imageVariations = if (imageVariations.isEmpty()) listOf(imageUrl) else imageVariations,
            narrative = narrative,
            expandedNarrative = if (expandedNarrative.isBlank()) narrative else expandedNarrative,
            tags = tags,
            category = category,
            mood = mood,
            isFavorite = isFavorite,
            isArchived = isArchived,
            personalNotes = personalNotes,
            symbols = listOf(
                SubconsciousSymbol("Levitation", "Freedom desire", "flight"),
                SubconsciousSymbol("Cascades", "Emotional flow", "waterfall_chart"),
                SubconsciousSymbol("Spire Citadel", "Higher goals", "castle")
            ),
            keyElements = DreamKeyElements(
                mainCharacters = "Myself, Unknown Guide",
                locations = "Sky Archipelago",
                objects = "Glowing Citadel",
                themes = "Exploration, Transience"
            ),
            interpretation = interpretation,
            estimatedRemTime = estimatedRemTime,
            clarityScore = clarityScore,
            archetype = archetype
        )
    }
}
