package com.example.data.model

data class SubconsciousSymbol(
    val name: String,
    val meaning: String,
    val iconName: String
)

data class DreamKeyElements(
    val mainCharacters: String,
    val locations: String,
    val objects: String,
    val themes: String = "Exploration, Freedom",
    val sensoryVisual: String = "Luminous chromatic gradients",
    val sensorySound: String = "Resonant nocturnal harmonies",
    val sensoryFeeling: String = "Weightless serenity"
)

data class Dream(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val lucidityLevel: String,
    val lucidityPercent: Int,
    val coherencePercent: Int,
    val emotionIntensityPercent: Int,
    val imageUrl: String,
    val imageVariations: List<String> = emptyList(),
    val narrative: String,
    val expandedNarrative: String = narrative,
    val tags: List<String>,
    val category: String,
    val mood: String, // "Wonder", "Serene", "Intrigue", "Euphoric", "Mystical"
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val personalNotes: String = "",
    val symbols: List<SubconsciousSymbol> = emptyList(),
    val keyElements: DreamKeyElements = DreamKeyElements(
        mainCharacters = "Myself, Unknown Guide",
        locations = "Sky Island, Ethereal Clouds",
        objects = "Glowing Citadel, Prisms",
        themes = "Exploration, Freedom"
    ),
    val interpretation: String = "A transcendent dream mirroring personal liberation. Flying or elevated terrain denotes an urge to step beyond routine boundaries.",
    val estimatedRemTime: String = "42 min",
    val clarityScore: String = "8.9 / 10",
    val archetype: String = "The Sage Archetype"
)
