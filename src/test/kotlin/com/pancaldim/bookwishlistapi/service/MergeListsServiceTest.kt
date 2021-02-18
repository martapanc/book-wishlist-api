package com.pancaldim.bookwishlistapi.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MergeListsServiceTest {

    @Test
    fun testTitlesAreSimilar() {
        val service = MergeListsService()
        assertTrue(service.titlesAreSimilar("A Curse So Dark and Lonely", "A Curse So Dark and Lonely (Cursebreakers, #1)"))
        assertTrue(service.titlesAreSimilar("La banalità del ma", "La banalità del male"))
        assertTrue(service.titlesAreSimilar("All Systems Red (The Murderbot Diaries, #1)", "ALL SYSTEMS RED"))
        assertTrue(service.titlesAreSimilar("American Gods", "(American Gods, #1)"))
        assertTrue(service.titlesAreSimilar("American Gods", "American Gods (American Gods, #1)"))
        assertTrue(service.titlesAreSimilar("Becoming", "Becoming: The Sunday Times Number One Bestseller"))
        assertTrue(service.titlesAreSimilar("Creative Writing Exercises For Dummies", "Creative Writing For Dummies"))
        assertTrue(service.titlesAreSimilar("Cynical Theories: How Activist Scholarship Made Everything about Race, Gender, and Identity—and Why This Harms Everybody",
            "Cynical Theories: How Universities Made Everything about Race, Gender, and Identity - And Why this Harms Everybody"))
        assertFalse(service.titlesAreSimilar("Shadow and Bone", "Siege and Storm (Shadow and Bone #2)"))
        assertFalse(service.titlesAreSimilar("The Hobbit", "Eragon"))
    }

    @Test
    fun testAuthorIsTheSame() {
        val service = MergeListsService()
        assertTrue(service.authorIsTheSame("Michelle Obama", "Michelle Obama"))
        assertTrue(service.authorIsTheSame("Neil Gaiman", "NEIL GAIMAN"))
        assertTrue(service.authorIsTheSame("Robert C. Martin", "Robert Martin"))
    }
}