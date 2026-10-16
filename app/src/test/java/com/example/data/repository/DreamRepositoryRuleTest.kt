package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.firebase.FirestoreDreamRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DreamRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createDream_authenticatedUser_succeeds() = runBlocking {
        val uid = signInTestUser("dharani_test@example.com")
        val repository = FirestoreDreamRepository(firestore)

        val dreamId = withTimeout(10000L) {
            repository.saveDream(
                title = "The Floating City",
                narrative = "Standing on a floating island...",
                mood = "Wonder",
                tags = listOf("Peaceful", "Fantasy"),
                imageUrl = "https://example.com/floating.jpg"
            )
        }

        assertNotNull(dreamId)
        assertTrue(dreamId.isNotEmpty())
    }

    @Test
    fun observeDreams_authenticatedUser_emitsSavedDream() = runBlocking {
        val uid = signInTestUser("dharani_flow@example.com")
        val repository = FirestoreDreamRepository(firestore)

        val dreamId = withTimeout(10000L) {
            repository.saveDream(
                title = "Moonlit Beach",
                narrative = "Walking across obsidian sand...",
                mood = "Serene",
                tags = listOf("Serene", "Ocean"),
                imageUrl = "https://example.com/moonlit.jpg"
            )
        }

        val emittedDreams = withTimeout(10000L) {
            repository.observeDreams().first { list ->
                list.any { it.title == "Moonlit Beach" }
            }
        }

        assertTrue(emittedDreams.any { it.title == "Moonlit Beach" })
    }

    @Test
    fun unauthenticatedAccess_failsFast() {
        auth.signOut()
        val repository = FirestoreDreamRepository(firestore)

        var failed = false
        try {
            repository.requireUserId()
        } catch (e: IllegalStateException) {
            failed = true
        }
        assertTrue("Unauthenticated access must throw IllegalStateException", failed)
    }
}
