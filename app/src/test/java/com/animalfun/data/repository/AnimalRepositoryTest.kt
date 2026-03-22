package com.animalfun.data.repository

import com.animalfun.data.local.AnimalDao
import com.animalfun.data.model.Animal
import com.animalfun.data.model.QuizQuestion
import com.animalfun.data.model.UserProgress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AnimalRepositoryTest {

    private lateinit var dao: AnimalDao
    private lateinit var repository: AnimalRepository

    private val testAnimal = Animal(
        id = 1,
        nameEn = "Dog",
        nameHe = "כלב",
        category = "Pets",
        imageRes = "dog",
        soundRes = "dog_sound",
        factsEn = listOf("Dogs are loyal"),
        factsHe = listOf("כלבים נאמנים"),
        riddleEn = "I bark",
        riddleHe = "אני נובח",
        dietEn = "Omnivore",
        dietHe = "אוכל כל",
        habitatEn = "Domestic",
        habitatHe = "ביתי",
        sizeInfo = "Medium"
    )

    private val testAnimal2 = testAnimal.copy(id = 2, nameEn = "Cat", nameHe = "חתול", category = "Pets")
    private val testAnimal3 = testAnimal.copy(id = 3, nameEn = "Lion", nameHe = "אריה", category = "Jungle")

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = AnimalRepository(dao)
    }

    // ── getAllAnimals ──

    @Test
    fun `getAllAnimals delegates to DAO and returns flow`() = runTest {
        val animals = listOf(testAnimal, testAnimal2)
        every { dao.getAllAnimals() } returns flowOf(animals)

        val result = repository.getAllAnimals().first()

        assertEquals(2, result.size)
        assertEquals("Dog", result[0].nameEn)
        verify(exactly = 1) { dao.getAllAnimals() }
    }

    @Test
    fun `getAllAnimals returns empty list when no animals`() = runTest {
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        val result = repository.getAllAnimals().first()

        assertEquals(0, result.size)
    }

    // ── getAnimalsByCategory ──

    @Test
    fun `getAnimalsByCategory delegates to DAO with correct category`() = runTest {
        val pets = listOf(testAnimal, testAnimal2)
        every { dao.getAnimalsByCategory("Pets") } returns flowOf(pets)

        val result = repository.getAnimalsByCategory("Pets").first()

        assertEquals(2, result.size)
        verify(exactly = 1) { dao.getAnimalsByCategory("Pets") }
    }

    @Test
    fun `getAnimalsByCategory returns empty for unknown category`() = runTest {
        every { dao.getAnimalsByCategory("Unknown") } returns flowOf(emptyList())

        val result = repository.getAnimalsByCategory("Unknown").first()

        assertEquals(0, result.size)
    }

    // ── getAnimalById ──

    @Test
    fun `getAnimalById returns animal when found`() = runTest {
        coEvery { dao.getAnimalById(1) } returns testAnimal

        val result = repository.getAnimalById(1)

        assertEquals(testAnimal, result)
        coVerify(exactly = 1) { dao.getAnimalById(1) }
    }

    @Test
    fun `getAnimalById returns null when not found`() = runTest {
        coEvery { dao.getAnimalById(999) } returns null

        val result = repository.getAnimalById(999)

        assertNull(result)
    }

    // ── searchAnimals ──

    @Test
    fun `searchAnimals delegates query to DAO`() = runTest {
        every { dao.searchAnimals("Dog") } returns flowOf(listOf(testAnimal))

        val result = repository.searchAnimals("Dog").first()

        assertEquals(1, result.size)
        assertEquals("Dog", result[0].nameEn)
        verify(exactly = 1) { dao.searchAnimals("Dog") }
    }

    @Test
    fun `searchAnimals returns empty for no match`() = runTest {
        every { dao.searchAnimals("xyz") } returns flowOf(emptyList())

        val result = repository.searchAnimals("xyz").first()

        assertEquals(0, result.size)
    }

    // ── Quiz operations ──

    @Test
    fun `getQuizQuestions delegates to DAO`() = runTest {
        val questions = listOf(
            QuizQuestion(
                id = 1, animalId = 1, questionType = "PICTURE",
                questionTextEn = "What is this?", questionTextHe = "מה זה?",
                correctAnswerEn = "Dog", correctAnswerHe = "כלב",
                wrongAnswersEn = listOf("Cat"), wrongAnswersHe = listOf("חתול")
            )
        )
        coEvery { dao.getQuizQuestions("PICTURE", 5) } returns questions

        val result = repository.getQuizQuestions("PICTURE", 5)

        assertEquals(1, result.size)
        coVerify(exactly = 1) { dao.getQuizQuestions("PICTURE", 5) }
    }

    // ── UserProgress operations ──

    @Test
    fun `getUserProgress delegates to DAO`() = runTest {
        val progress = UserProgress(animalId = 1, explored = true, starsEarned = 2)
        every { dao.getUserProgress(1) } returns flowOf(progress)

        val result = repository.getUserProgress(1).first()

        assertEquals(2, result?.starsEarned)
        verify(exactly = 1) { dao.getUserProgress(1) }
    }

    @Test
    fun `upsertProgress delegates to DAO`() = runTest {
        val progress = UserProgress(animalId = 1, explored = true)
        coEvery { dao.upsertProgress(progress) } returns Unit

        repository.upsertProgress(progress)

        coVerify(exactly = 1) { dao.upsertProgress(progress) }
    }

    @Test
    fun `getTotalStars delegates to DAO`() = runTest {
        every { dao.getTotalStars() } returns flowOf(15)

        val result = repository.getTotalStars().first()

        assertEquals(15, result)
    }

    @Test
    fun `deleteAllProgress delegates to DAO`() = runTest {
        coEvery { dao.deleteAllProgress() } returns Unit

        repository.deleteAllProgress()

        coVerify(exactly = 1) { dao.deleteAllProgress() }
    }
}
