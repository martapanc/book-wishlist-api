package com.pancaldim.bookwishlistapi.model

data class Book(
    var title: String,
    var author: String,
    val coverUrl: String? = null,
    val isbn13: String? = null,
    val tags: List<String> = listOf(),
    val source: Source,
    val link: String? = null
)

enum class Source {
    Amazon, Goodreads
}