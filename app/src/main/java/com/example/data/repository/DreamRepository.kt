package com.example.data.repository

import com.example.data.model.Dream
import com.example.data.model.DreamKeyElements
import com.example.data.model.SubconsciousSymbol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object DreamRepository {

    private val initialDreams = listOf(
        Dream(
            id = "dream-1",
            title = "The Floating City",
            date = "Oct 15, 2026",
            time = "5:42 AM",
            lucidityLevel = "Lucid Level II",
            lucidityPercent = 85,
            coherencePercent = 92,
            emotionIntensityPercent = 80,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCG-GkxspAtuMMeP_Vxo_CKOySom5q3hy2mWK6JWNP4URzO_elJGHdhsza8CSAlvFB1yvFYV-h9UfLOaBudexHByB-vBAyECtA2kyCm3Q58hYqnKSJQJeLd6OTih6-KEyH5z8lKDQBy3FwNIe3qV4Y8Xe4y5eTiDJoxAnzwOjFcaXjFHVbVmgjF1BYnDjb8OQfbNt7L62TVYoCD-V32kNyeR5hcJXeyQarc_ObJuV6E2IOEDFj-M-Pa5g",
            imageVariations = listOf(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCG-GkxspAtuMMeP_Vxo_CKOySom5q3hy2mWK6JWNP4URzO_elJGHdhsza8CSAlvFB1yvFYV-h9UfLOaBudexHByB-vBAyECtA2kyCm3Q58hYqnKSJQJeLd6OTih6-KEyH5z8lKDQBy3FwNIe3qV4Y8Xe4y5eTiDJoxAnzwOjFcaXjFHVbVmgjF1BYnDjb8OQfbNt7L62TVYoCD-V32kNyeR5hcJXeyQarc_ObJuV6E2IOEDFj-M-Pa5g",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCohCzS2WFgGV-E7sK4qhF6a_pxKo75719PIwQyA_rOhpXN3huYhHuUeJTpkOABldpWViAxsEji_RsDgDp4Six5Q6e-Q3WiAeyQ2f5oIvRPPWeUt5kg49dfTCma8ZPOrW0v9fcL3KlmUBa6bLdjvAxfv0ED84y5NLjmi5sigiyTU_4CzvWeY08LKyVhoMcHsA1TQFB57mEPa_x-jgPFVa76KnoR8f6n9E5MzJZeWBy2RFOU3UIB4ImgEg",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuAE8xKGbnM8YGXy23pDwhOy1LI5NU0R6S2AZFaSn_a8CFRdlNZsQURvm1WPyz5ArFaEQ5Tb5HAhcI1hewuw5drTAgyYOOtnBqnbWpmgAO2B31_Mic-UUHE_byIMykKFg2_CSiP8uR12zHUjkvFzpAkbpF1BQ9XcDosNilSQJoE685WYHgo4xAnBQJyT6sG2VBd9RHBdXpkxMLQoX_YyL-K3NalCdre1P_elbbin93haXTyV9oOWPFT9fg",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCgExoxV5JunWRO13inYZsS06Tj7BvUScNqCE2LMYEment6PerPeDmI9vjlbi9iEe8iRVCBmIynextwz1Gyq5aCcPxJsNRbOMmi6XxoeSml1ycPED6KDux4Nx0OcD9Efk2tBBB_7-fSpFpw1zdwKYeduRgyLm3thyHELVchoD9iTY2AFyjLqLvK1kmZBaexufTHZHz6teC5StSg7jwhOmbkHBM0Ts3mkK8i4jPYsc1SAN_C4BhmxKoaQ"
            ),
            narrative = "I was standing on a floating island suspended gracefully above perpetual clouds. A serene, futuristic citadel shimmered with luminous spires and ethereal turquoise waterfalls spilling endlessly into pastel violet mists. I felt intensely peaceful yet electric with discovery as an enigmatic hooded figure handed me a golden prism...",
            expandedNarrative = "I was standing on a floating island suspended gracefully above perpetual clouds. A serene, futuristic citadel shimmered with luminous spires and ethereal turquoise waterfalls spilling endlessly into pastel violet mists. I felt intensely peaceful yet electric with discovery as an enigmatic hooded figure handed me a golden prism. The prism refracted impossible colors across the night sky, harmonizing with harmonic chimes that resonated within my heart. I stepped toward the edge and realized I could step directly onto the air without falling.",
            tags = listOf("Peaceful", "Fantasy", "Exploration", "Floating City", "Magic"),
            category = "Fantasy Realm",
            mood = "Wonder",
            isFavorite = true,
            personalNotes = "This dream felt very real. I want to remember the feeling of freedom in this dream.",
            symbols = listOf(
                SubconsciousSymbol("Levitation", "Freedom desire", "flight"),
                SubconsciousSymbol("Cascades", "Emotional flow", "waterfall_chart"),
                SubconsciousSymbol("Spire Citadel", "Higher goals", "castle")
            ),
            keyElements = DreamKeyElements(
                mainCharacters = "Myself, Unknown Cloaked Guide",
                locations = "Floating Sky Archipelago, Sunset Nebula Citadel",
                objects = "Hovering Islands, Endless Waterfalls, Prismatic Crystal",
                themes = "Exploration, Boundless Elevation, Overcoming Stagnation"
            ),
            interpretation = "A transcendent dream mirroring personal liberation. Flying or elevated terrain denotes an urge to step beyond routine boundaries. Your subconscious is channeling feelings of peace, high creative momentum, and openness to intuitive guidance.",
            estimatedRemTime = "42 min (Deep REM)",
            clarityScore = "8.9 / 10",
            archetype = "The Sage Archetype"
        ),
        Dream(
            id = "dream-2",
            title = "Moonlit Beach",
            date = "Oct 12, 2026",
            time = "4:15 AM",
            lucidityLevel = "Lucid Level I",
            lucidityPercent = 64,
            coherencePercent = 88,
            emotionIntensityPercent = 70,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDGEPv89w-L25YTYsEudaweIDG-m-GXUeU2x-prMMEhS4VCzq4p7SmdVL_MHJ7BPZRnTOZI_YVm8Vrt-kZ7IyPdELgFmadHjFclIRfvw98dp-RdtA3b5wY05wgxcAIW5_TjgjfM3KmMBVy1zV7_VeKqpfdeQIVZm8hkBxzyj29IC79YANovZ9ehLBQc8oksDRQMJMUwbPJW9LbTP0bFhuznfstw2-dpue51Eo7s006NCq76HSFH__xZ3w",
            imageVariations = listOf(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuDGEPv89w-L25YTYsEudaweIDG-m-GXUeU2x-prMMEhS4VCzq4p7SmdVL_MHJ7BPZRnTOZI_YVm8Vrt-kZ7IyPdELgFmadHjFclIRfvw98dp-RdtA3b5wY05wgxcAIW5_TjgjfM3KmMBVy1zV7_VeKqpfdeQIVZm8hkBxzyj29IC79YANovZ9ehLBQc8oksDRQMJMUwbPJW9LbTP0bFhuznfstw2-dpue51Eo7s006NCq76HSFH__xZ3w"
            ),
            narrative = "Walking across obsidian sands under a colossal glowing crescent moon. The waves pulsed with neon cyan bioluminescence, hovering in droplets above the surf.",
            expandedNarrative = "Walking across obsidian sands under a colossal glowing crescent moon. The waves pulsed with neon cyan bioluminescence, hovering in droplets above the surf. Every breath felt crystal pure, dissolving stress.",
            tags = listOf("Peaceful", "Ocean", "Serene", "Bioluminescence"),
            category = "Oceanic",
            mood = "Serene",
            isFavorite = false,
            personalNotes = "Deep restoration and calm.",
            symbols = listOf(
                SubconsciousSymbol("Tides", "Rhythms of healing", "waves"),
                SubconsciousSymbol("Full Moon", "Intuition peek", "nightlight")
            ),
            keyElements = DreamKeyElements(
                mainCharacters = "Myself alone",
                locations = "Midnight Coastal Sanctuary",
                objects = "Floating droplets, Phosphor shells",
                themes = "Inner Silence, Renewal"
            ),
            interpretation = "Water symbolizes emotional harmony and unburdened intuition. The glowing sea signifies hidden potential awakening.",
            estimatedRemTime = "36 min",
            clarityScore = "7.8 / 10",
            archetype = "The Healer Archetype"
        ),
        Dream(
            id = "dream-3",
            title = "Mysterious Forest",
            date = "Oct 10, 2026",
            time = "6:05 AM",
            lucidityLevel = "Non-Lucid",
            lucidityPercent = 45,
            coherencePercent = 82,
            emotionIntensityPercent = 75,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCKqL7XJhRxt4S4XcphsZ1rGVKZazMvili-K5l-DWk4dmLNDsGiyP1WD1cyS9hfAE8uOOGAL-D85AivwrNU52WZBm7tvEe2gNo4IaPTFX-IyX5BbB9491l-a28vyvKEbQOrUxRc0oychOOazYTdusGlfzndaxyr0y0RJrWwwfL0cUonLi4U3L1cRGQCbFda7MuWVzhnmwGWg0DKCIYpD92SNNVsJOQ-i3jBiiiDGUBsEMZbSSmBQhzozw",
            imageVariations = listOf(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCKqL7XJhRxt4S4XcphsZ1rGVKZazMvili-K5l-DWk4dmLNDsGiyP1WD1cyS9hfAE8uOOGAL-D85AivwrNU52WZBm7tvEe2gNo4IaPTFX-IyX5BbB9491l-a28vyvKEbQOrUxRc0oychOOazYTdusGlfzndaxyr0y0RJrWwwfL0cUonLi4U3L1cRGQCbFda7MuWVzhnmwGWg0DKCIYpD92SNNVsJOQ-i3jBiiiDGUBsEMZbSSmBQhzozw"
            ),
            narrative = "Giant ancient roots spiraled upwards into luminous purple canopies. Glowing mushrooms guided a winding trail illuminated by wandering spirit wisps.",
            expandedNarrative = "Giant ancient roots spiraled upwards into luminous purple canopies. Glowing mushrooms guided a winding trail illuminated by wandering spirit wisps. The trees whispered ancient riddles in soft echoes.",
            tags = listOf("Mystery", "Forest", "Wisps", "Intrigue"),
            category = "Nature",
            mood = "Intrigue",
            isFavorite = true,
            personalNotes = "I should trust unmapped journeys.",
            symbols = listOf(
                SubconsciousSymbol("Roots", "Ancestral grounding", "park"),
                SubconsciousSymbol("Wisps", "Subconscious guides", "flare")
            ),
            keyElements = DreamKeyElements(
                mainCharacters = "Forest spirits, Myself",
                locations = "Twilit Sylvan Sanctuary",
                objects = "Bioluminescent Flora, Ancient Oak",
                themes = "Curiosity, Hidden Paths"
            ),
            interpretation = "Entering a dense yet glowing forest signifies an exploration of the unknown parts of your psyche without fear.",
            estimatedRemTime = "48 min",
            clarityScore = "8.2 / 10",
            archetype = "The Seeker Archetype"
        ),
        Dream(
            id = "dream-4",
            title = "Crystal Caverns",
            date = "Oct 08, 2026",
            time = "3:30 AM",
            lucidityLevel = "Lucid Level III",
            lucidityPercent = 92,
            coherencePercent = 95,
            emotionIntensityPercent = 90,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCgExoxV5JunWRO13inYZsS06Tj7BvUScNqCE2LMYEment6PerPeDmI9vjlbi9iEe8iRVCBmIynextwz1Gyq5aCcPxJsNRbOMmi6XxoeSml1ycPED6KDux4Nx0OcD9Efk2tBBB_7-fSpFpw1zdwKYeduRgyLm3thyHELVchoD9iTY2AFyjLqLvK1kmZBaexufTHZHz6teC5StSg7jwhOmbkHBM0Ts3mkK8i4jPYsc1SAN_C4BhmxKoaQ",
            imageVariations = listOf(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCgExoxV5JunWRO13inYZsS06Tj7BvUScNqCE2LMYEment6PerPeDmI9vjlbi9iEe8iRVCBmIynextwz1Gyq5aCcPxJsNRbOMmi6XxoeSml1ycPED6KDux4Nx0OcD9Efk2tBBB_7-fSpFpw1zdwKYeduRgyLm3thyHELVchoD9iTY2AFyjLqLvK1kmZBaexufTHZHz6teC5StSg7jwhOmbkHBM0Ts3mkK8i4jPYsc1SAN_C4BhmxKoaQ"
            ),
            narrative = "Towering amethyst and rose quartz pillars sang in harmonic chords. I could touch the crystal surfaces and feel vibrations of pure euphoria.",
            expandedNarrative = "Towering amethyst and rose quartz pillars sang in harmonic chords. I could touch the crystal surfaces and feel vibrations of pure euphoria. The echoing sounds resonated through every cell, completely lucid.",
            tags = listOf("Euphoric", "Crystals", "Lucid", "Sound"),
            category = "Lucid Dreams",
            mood = "Euphoric",
            isFavorite = true,
            personalNotes = "Highest lucidity ever recorded! Felt like high-dimensional harmonics.",
            symbols = listOf(
                SubconsciousSymbol("Amethyst", "Spiritual lucidity", "diamond"),
                SubconsciousSymbol("Harmonics", "Attunement", "music_note")
            ),
            keyElements = DreamKeyElements(
                mainCharacters = "Conscious self",
                locations = "Subterranean Prism Chamber",
                objects = "Singing Quartz Monoliths",
                themes = "Vibration, Cosmic Alignment"
            ),
            interpretation = "Gems and crystals indicate crystallization of a key life insight or sudden clarity after confusion.",
            estimatedRemTime = "51 min",
            clarityScore = "9.4 / 10",
            archetype = "The Mystic Archetype"
        )
    )

    private val _dreams = MutableStateFlow<List<Dream>>(initialDreams)
    val dreams: StateFlow<List<Dream>> = _dreams.asStateFlow()

    fun getDreamById(id: String): Dream? {
        return _dreams.value.find { it.id == id }
    }

    fun toggleFavorite(dreamId: String) {
        _dreams.value = _dreams.value.map { dream ->
            if (dream.id == dreamId) {
                dream.copy(isFavorite = !dream.isFavorite)
            } else {
                dream
            }
        }
    }

    fun updateNotes(dreamId: String, notes: String, isFav: Boolean) {
        _dreams.value = _dreams.value.map { dream ->
            if (dream.id == dreamId) {
                dream.copy(personalNotes = notes, isFavorite = isFav)
            } else {
                dream
            }
        }
    }

    fun updateSelectedImage(dreamId: String, newImageUrl: String) {
        _dreams.value = _dreams.value.map { dream ->
            if (dream.id == dreamId) {
                dream.copy(imageUrl = newImageUrl)
            } else {
                dream
            }
        }
    }

    fun addTag(dreamId: String, tag: String) {
        _dreams.value = _dreams.value.map { dream ->
            if (dream.id == dreamId && !dream.tags.contains(tag)) {
                dream.copy(tags = dream.tags + tag)
            } else {
                dream
            }
        }
    }

    fun deleteDream(dreamId: String) {
        _dreams.value = _dreams.value.filter { it.id != dreamId }
    }

    fun archiveDream(dreamId: String, isArchived: Boolean) {
        _dreams.value = _dreams.value.map {
            if (it.id == dreamId) it.copy(isArchived = isArchived) else it
        }
    }

    fun saveNewDream(
        title: String,
        narrative: String,
        mood: String,
        tags: List<String>,
        imageUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuAE8xKGbnM8YGXy23pDwhOy1LI5NU0R6S2AZFaSn_a8CFRdlNZsQURvm1WPyz5ArFaEQ5Tb5HAhcI1hewuw5drTAgyYOOtnBqnbWpmgAO2B31_Mic-UUHE_byIMykKFg2_CSiP8uR12zHUjkvFzpAkbpF1BQ9XcDosNilSQJoE685WYHgo4xAnBQJyT6sG2VBd9RHBdXpkxMLQoX_YyL-K3NalCdre1P_elbbin93haXTyV9oOWPFT9fg",
        archetype: String = "The Cosmic Traveler",
        lucidityLevel: String = "Lucid Level II",
        lucidityPercent: Int = 88,
        coherencePercent: Int = 94,
        emotionIntensityPercent: Int = 85,
        interpretation: String = "An exceptionally lucid dream indicating clarity of purpose and celestial inspiration.",
        symbols: List<SubconsciousSymbol> = listOf(
            SubconsciousSymbol("Ascension", "Elevation of consciousness", "flight_takeoff"),
            SubconsciousSymbol("Golden Light", "Higher clarity", "light_mode")
        ),
        keyElements: DreamKeyElements = DreamKeyElements(
            mainCharacters = "Myself, Celestial Entities",
            locations = "Astral Horizon",
            objects = "Luminescent Portals",
            themes = "Liberation, Rebirth"
        )
    ): Dream {
        val newDream = Dream(
            id = "dream-${UUID.randomUUID()}",
            title = if (title.isBlank()) "Midnight Transcendence" else title,
            date = "Oct 16, 2026",
            time = "6:10 AM",
            lucidityLevel = lucidityLevel,
            lucidityPercent = lucidityPercent,
            coherencePercent = coherencePercent,
            emotionIntensityPercent = emotionIntensityPercent,
            imageUrl = imageUrl,
            imageVariations = listOf(
                imageUrl,
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCohCzS2WFgGV-E7sK4qhF6a_pxKo75719PIwQyA_rOhpXN3huYhHuUeJTpkOABldpWViAxsEji_RsDgDp4Six5Q6e-Q3WiAeyQ2f5oIvRPPWeUt5kg49dfTCma8ZPOrW0v9fcL3KlmUBa6bLdjvAxfv0ED84y5NLjmi5sigiyTU_4CzvWeY08LKyVhoMcHsA1TQFB57mEPa_x-jgPFVa76KnoR8f6n9E5MzJZeWBy2RFOU3UIB4ImgEg",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCG-GkxspAtuMMeP_Vxo_CKOySom5q3hy2mWK6JWNP4URzO_elJGHdhsza8CSAlvFB1yvFYV-h9UfLOaBudexHByB-vBAyECtA2kyCm3Q58hYqnKSJQJeLd6OTih6-KEyH5z8lKDQBy3FwNIe3qV4Y8Xe4y5eTiDJoxAnzwOjFcaXjFHVbVmgjF1BYnDjb8OQfbNt7L62TVYoCD-V32kNyeR5hcJXeyQarc_ObJuV6E2IOEDFj-M-Pa5g"
            ),
            narrative = narrative,
            expandedNarrative = narrative + " The subconscious realm unfolded with vivid clarity, unlocking deep emotional release and celestial inspiration.",
            tags = if (tags.isEmpty()) listOf("Peaceful", "Transcendence", "Lucid") else tags,
            category = "Fantasy Realm",
            mood = mood,
            isFavorite = true,
            personalNotes = "Captured freshly upon awakening.",
            symbols = symbols,
            keyElements = keyElements,
            interpretation = interpretation,
            estimatedRemTime = "45 min",
            clarityScore = "${coherencePercent / 10.0} / 10",
            archetype = archetype
        )
        _dreams.value = listOf(newDream) + _dreams.value
        return newDream
    }
}
