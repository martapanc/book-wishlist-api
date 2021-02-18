package com.pancaldim.bookwishlistapi.model

data class Book(
    val title: String,
    val author: String,
    val coauthors: List<String> = listOf(),
    val coverUrl: String? = null,
    val isbn13: String? = null,
    val source: Source
)

enum class Source {
    Amazon, Goodreads
}