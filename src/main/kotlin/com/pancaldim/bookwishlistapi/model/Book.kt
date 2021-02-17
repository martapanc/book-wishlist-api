package com.pancaldim.bookwishlistapi.model

data class Book(
    val title: String,
    val author: String,
    val coauthors: List<String> = listOf(),
    val coverUrl: String? = "",
    val isbn13: String? = ""
)
