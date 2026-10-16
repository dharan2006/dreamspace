package com.example.data.firebase

import android.content.Context
import com.example.R
import com.example.data.model.Dream
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreDreamRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth get() = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore dreams.")
    }

    fun observeDreams(): Flow<List<Dream>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/dreams"
        emitAll(
            db.collection("users").document(uid).collection("dreams")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(FirestoreDream::class.java).map { it.toDomainDream() }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun saveDream(
        title: String,
        narrative: String,
        mood: String,
        tags: List<String>,
        imageUrl: String,
        archetype: String = "The Cosmic Traveler",
        lucidityLevel: String = "Lucid Level II",
        lucidityPercent: Int = 85,
        coherencePercent: Int = 92,
        emotionIntensityPercent: Int = 80,
        interpretation: String = "An exceptionally lucid dream indicating clarity of purpose and celestial inspiration."
    ): String {
        val uid = requireUserId()
        val path = "users/$uid/dreams"
        val collectionRef = db.collection("users").document(uid).collection("dreams")
        val docRef = collectionRef.document()

        val dreamData = mapOf(
            "userId" to uid,
            "title" to title,
            "narrative" to narrative,
            "expandedNarrative" to (narrative + " The subconscious realm unfolded with celestial clarity."),
            "date" to "Oct 16, 2026",
            "time" to "6:15 AM",
            "mood" to mood,
            "category" to "Fantasy Realm",
            "lucidityLevel" to lucidityLevel,
            "lucidityPercent" to lucidityPercent,
            "coherencePercent" to coherencePercent,
            "emotionIntensityPercent" to emotionIntensityPercent,
            "imageUrl" to imageUrl,
            "imageVariations" to listOf(imageUrl),
            "tags" to tags,
            "isFavorite" to true,
            "isArchived" to false,
            "personalNotes" to "Captured freshly upon awakening into cloud sanctuary.",
            "interpretation" to interpretation,
            "estimatedRemTime" to "45 min",
            "clarityScore" to "${(coherencePercent / 10.0)} / 10",
            "archetype" to archetype,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(dreamData).await()
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            throw e
        }
    }

    suspend fun deleteDream(dreamId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    suspend fun archiveDream(dreamId: String, isArchived: Boolean) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.update(
                mapOf(
                    "isArchived" to isArchived,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun updateNotes(dreamId: String, notes: String, isFav: Boolean) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.update(
                mapOf(
                    "personalNotes" to notes,
                    "isFavorite" to isFav,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun toggleFavorite(dreamId: String, currentFav: Boolean) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.update(
                mapOf(
                    "isFavorite" to !currentFav,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun selectImageVariation(dreamId: String, newImageUrl: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.update(
                mapOf(
                    "imageUrl" to newImageUrl,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun addTag(dreamId: String, tag: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("dreams").document(dreamId)
        try {
            docRef.update(
                mapOf(
                    "tags" to FieldValue.arrayUnion(tag),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun seedInitialDreamsIfEmpty() {
        val uid = requireUserId()
        val collection = db.collection("users").document(uid).collection("dreams")
        try {
            val existing = collection.limit(1).get().await()
            if (existing.isEmpty) {
                // Seed the 4 initial dreams
                val seeds = listOf(
                    mapOf(
                        "userId" to uid,
                        "title" to "The Floating City",
                        "narrative" to "I was standing on a floating island suspended gracefully above perpetual clouds. A serene, futuristic citadel shimmered with luminous spires and ethereal turquoise waterfalls spilling endlessly into pastel violet mists...",
                        "expandedNarrative" to "I was standing on a floating island suspended gracefully above perpetual clouds. A serene, futuristic citadel shimmered with luminous spires and ethereal turquoise waterfalls spilling endlessly into pastel violet mists. I felt intensely peaceful yet electric with discovery as an enigmatic hooded figure handed me a golden prism.",
                        "date" to "Oct 15, 2026",
                        "time" to "5:42 AM",
                        "mood" to "Wonder",
                        "category" to "Fantasy Realm",
                        "lucidityLevel" to "Lucid Level II",
                        "lucidityPercent" to 85,
                        "coherencePercent" to 92,
                        "emotionIntensityPercent" to 80,
                        "imageUrl" to "https://lh3.googleusercontent.com/aida-public/AB6AXuCG-GkxspAtuMMeP_Vxo_CKOySom5q3hy2mWK6JWNP4URzO_elJGHdhsza8CSAlvFB1yvFYV-h9UfLOaBudexHByB-vBAyECtA2kyCm3Q58hYqnKSJQJeLd6OTih6-KEyH5z8lKDQBy3FwNIe3qV4Y8Xe4y5eTiDJoxAnzwOjFcaXjFHVbVmgjF1BYnDjb8OQfbNt7L62TVYoCD-V32kNyeR5hcJXeyQarc_ObJuV6E2IOEDFj-M-Pa5g",
                        "imageVariations" to listOf(
                            "https://lh3.googleusercontent.com/aida-public/AB6AXuCG-GkxspAtuMMeP_Vxo_CKOySom5q3hy2mWK6JWNP4URzO_elJGHdhsza8CSAlvFB1yvFYV-h9UfLOaBudexHByB-vBAyECtA2kyCm3Q58hYqnKSJQJeLd6OTih6-KEyH5z8lKDQBy3FwNIe3qV4Y8Xe4y5eTiDJoxAnzwOjFcaXjFHVbVmgjF1BYnDjb8OQfbNt7L62TVYoCD-V32kNyeR5hcJXeyQarc_ObJuV6E2IOEDFj-M-Pa5g",
                            "https://lh3.googleusercontent.com/aida-public/AB6AXuCohCzS2WFgGV-E7sK4qhF6a_pxKo75719PIwQyA_rOhpXN3huYhHuUeJTpkOABldpWViAxsEji_RsDgDp4Six5Q6e-Q3WiAeyQ2f5oIvRPPWeUt5kg49dfTCma8ZPOrW0v9fcL3KlmUBa6bLdjvAxfv0ED84y5NLjmi5sigiyTU_4CzvWeY08LKyVhoMcHsA1TQFB57mEPa_x-jgPFVa76KnoR8f6n9E5MzJZeWBy2RFOU3UIB4ImgEg"
                        ),
                        "tags" to listOf("Peaceful", "Fantasy", "Exploration", "Floating City", "Magic"),
                        "isFavorite" to true,
                        "isArchived" to false,
                        "personalNotes" to "This dream felt very real. I want to remember the feeling of freedom in this dream.",
                        "interpretation" to "A transcendent dream mirroring personal liberation. Flying or elevated terrain denotes an urge to step beyond routine boundaries.",
                        "estimatedRemTime" to "42 min (Deep REM)",
                        "clarityScore" to "8.9 / 10",
                        "archetype" to "The Sage Archetype",
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    ),
                    mapOf(
                        "userId" to uid,
                        "title" to "Moonlit Beach",
                        "narrative" to "Walking across obsidian sands under a colossal glowing crescent moon. The waves pulsed with neon cyan bioluminescence, hovering in droplets above the surf.",
                        "expandedNarrative" to "Walking across obsidian sands under a colossal glowing crescent moon. The waves pulsed with neon cyan bioluminescence, hovering in droplets above the surf. Every breath felt crystal pure, dissolving stress.",
                        "date" to "Oct 12, 2026",
                        "time" to "4:15 AM",
                        "mood" to "Serene",
                        "category" to "Oceanic",
                        "lucidityLevel" to "Lucid Level I",
                        "lucidityPercent" to 64,
                        "coherencePercent" to 88,
                        "emotionIntensityPercent" to 70,
                        "imageUrl" to "https://lh3.googleusercontent.com/aida-public/AB6AXuDGEPv89w-L25YTYsEudaweIDG-m-GXUeU2x-prMMEhS4VCzq4p7SmdVL_MHJ7BPZRnTOZI_YVm8Vrt-kZ7IyPdELgFmadHjFclIRfvw98dp-RdtA3b5wY05wgxcAIW5_TjgjfM3KmMBVy1zV7_VeKqpfdeQIVZm8hkBxzyj29IC79YANovZ9ehLBQc8oksDRQMJMUwbPJW9LbTP0bFhuznfstw2-dpue51Eo7s006NCq76HSFH__xZ3w",
                        "imageVariations" to listOf("https://lh3.googleusercontent.com/aida-public/AB6AXuDGEPv89w-L25YTYsEudaweIDG-m-GXUeU2x-prMMEhS4VCzq4p7SmdVL_MHJ7BPZRnTOZI_YVm8Vrt-kZ7IyPdELgFmadHjFclIRfvw98dp-RdtA3b5wY05wgxcAIW5_TjgjfM3KmMBVy1zV7_VeKqpfdeQIVZm8hkBxzyj29IC79YANovZ9ehLBQc8oksDRQMJMUwbPJW9LbTP0bFhuznfstw2-dpue51Eo7s006NCq76HSFH__xZ3w"),
                        "tags" to listOf("Peaceful", "Ocean", "Serene", "Bioluminescence"),
                        "isFavorite" to false,
                        "isArchived" to false,
                        "personalNotes" to "Deep restoration and calm.",
                        "interpretation" to "Water symbolizes emotional harmony and unburdened intuition. The glowing sea signifies hidden potential awakening.",
                        "estimatedRemTime" to "36 min",
                        "clarityScore" to "7.8 / 10",
                        "archetype" to "The Healer Archetype",
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    ),
                    mapOf(
                        "userId" to uid,
                        "title" to "Mysterious Forest",
                        "narrative" to "Giant ancient roots spiraled upwards into luminous purple canopies. Glowing mushrooms guided a winding trail illuminated by wandering spirit wisps.",
                        "expandedNarrative" to "Giant ancient roots spiraled upwards into luminous purple canopies. Glowing mushrooms guided a winding trail illuminated by wandering spirit wisps. The trees whispered ancient riddles in soft echoes.",
                        "date" to "Oct 10, 2026",
                        "time" to "6:05 AM",
                        "mood" to "Intrigue",
                        "category" to "Nature",
                        "lucidityLevel" to "Non-Lucid",
                        "lucidityPercent" to 45,
                        "coherencePercent" to 82,
                        "emotionIntensityPercent" to 75,
                        "imageUrl" to "https://lh3.googleusercontent.com/aida-public/AB6AXuCKqL7XJhRxt4S4XcphsZ1rGVKZazMvili-K5l-DWk4dmLNDsGiyP1WD1cyS9hfAE8uOOGAL-D85AivwrNU52WZBm7tvEe2gNo4IaPTFX-IyX5BbB9491l-a28vyvKEbQOrUxRc0oychOOazYTdusGlfzndaxyr0y0RJrWwwfL0cUonLi4U3L1cRGQCbFda7MuWVzhnmwGWg0DKCIYpD92SNNVsJOQ-i3jBiiiDGUBsEMZbSSmBQhzozw",
                        "imageVariations" to listOf("https://lh3.googleusercontent.com/aida-public/AB6AXuCKqL7XJhRxt4S4XcphsZ1rGVKZazMvili-K5l-DWk4dmLNDsGiyP1WD1cyS9hfAE8uOOGAL-D85AivwrNU52WZBm7tvEe2gNo4IaPTFX-IyX5BbB9491l-a28vyvKEbQOrUxRc0oychOOazYTdusGlfzndaxyr0y0RJrWwwfL0cUonLi4U3L1cRGQCbFda7MuWVzhnmwGWg0DKCIYpD92SNNVsJOQ-i3jBiiiDGUBsEMZbSSmBQhzozw"),
                        "tags" to listOf("Mystery", "Forest", "Wisps", "Intrigue"),
                        "isFavorite" to true,
                        "isArchived" to false,
                        "personalNotes" to "I should trust unmapped journeys.",
                        "interpretation" to "Entering a dense yet glowing forest signifies an exploration of the unknown parts of your psyche without fear.",
                        "estimatedRemTime" to "48 min",
                        "clarityScore" to "8.2 / 10",
                        "archetype" to "The Seeker Archetype",
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    ),
                    mapOf(
                        "userId" to uid,
                        "title" to "Crystal Caverns",
                        "narrative" to "Towering amethyst and rose quartz pillars sang in harmonic chords. I could touch the crystal surfaces and feel vibrations of pure euphoria.",
                        "expandedNarrative" to "Towering amethyst and rose quartz pillars sang in harmonic chords. I could touch the crystal surfaces and feel vibrations of pure euphoria. The echoing sounds resonated through every cell, completely lucid.",
                        "date" to "Oct 08, 2026",
                        "time" to "3:30 AM",
                        "mood" to "Euphoric",
                        "category" to "Lucid Dreams",
                        "lucidityLevel" to "Lucid Level III",
                        "lucidityPercent" to 92,
                        "coherencePercent" to 95,
                        "emotionIntensityPercent" to 90,
                        "imageUrl" to "https://lh3.googleusercontent.com/aida-public/AB6AXuCgExoxV5JunWRO13inYZsS06Tj7BvUScNqCE2LMYEment6PerPeDmI9vjlbi9iEe8iRVCBmIynextwz1Gyq5aCcPxJsNRbOMmi6XxoeSml1ycPED6KDux4Nx0OcD9Efk2tBBB_7-fSpFpw1zdwKYeduRgyLm3thyHELVchoD9iTY2AFyjLqLvK1kmZBaexufTHZHz6teC5StSg7jwhOmbkHBM0Ts3mkK8i4jPYsc1SAN_C4BhmxKoaQ",
                        "imageVariations" to listOf("https://lh3.googleusercontent.com/aida-public/AB6AXuCgExoxV5JunWRO13inYZsS06Tj7BvUScNqCE2LMYEment6PerPeDmI9vjlbi9iEe8iRVCBmIynextwz1Gyq5aCcPxJsNRbOMmi6XxoeSml1ycPED6KDux4Nx0OcD9Efk2tBBB_7-fSpFpw1zdwKYeduRgyLm3thyHELVchoD9iTY2AFyjLqLvK1kmZBaexufTHZHz6teC5StSg7jwhOmbkHBM0Ts3mkK8i4jPYsc1SAN_C4BhmxKoaQ"),
                        "tags" to listOf("Euphoric", "Crystals", "Lucid", "Sound"),
                        "isFavorite" to true,
                        "isArchived" to false,
                        "personalNotes" to "Highest lucidity ever recorded! Felt like high-dimensional harmonics.",
                        "interpretation" to "Gems and crystals indicate crystallization of a key life insight or sudden clarity after confusion.",
                        "estimatedRemTime" to "51 min",
                        "clarityScore" to "9.4 / 10",
                        "archetype" to "The Mystic Archetype",
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )

                for (seed in seeds) {
                    collection.add(seed).await()
                }
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, collection.path)
        }
    }
}
