package com.pancaldim.bookwishlistapi.service

import com.pancaldim.bookwishlistapi.model.Book
import net.ricecode.similarity.JaroWinklerStrategy
import net.ricecode.similarity.StringSimilarityServiceImpl
import org.springframework.stereotype.Service

@Service
class MergeListsService {

    fun getMergedList(apiKey: Secret): List<Book> {
        val amazonService = AmazonService()
        val techBooks = amazonService.scrapeListFromPrintTemplate(WishList.Tech)
        val fictionBooks = amazonService.scrapeListFromPrintTemplate(WishList.Fiction)
        val nonFictionBooks = amazonService.scrapeListFromPrintTemplate(WishList.NonFiction)
        val goodreadsBooks = GoodreadsService().retrieveToReadList(apiKey)

        val mergedList = techBooks + fictionBooks + nonFictionBooks + goodreadsBooks
        val sorted = mergedList.sortedBy { it.title }.toMutableList()

        var i = 0
        while (i < sorted.size) {
            val currentBook = sorted[i]
            val duplicates = mutableListOf<Book>()
            val restOfBooks = sorted.minus(currentBook)
            restOfBooks.filterTo(duplicates) { isProbableDuplicate(currentBook, it) }

            if (duplicates.isNotEmpty()) {
                sorted.removeAll(duplicates)
            }
            i++
        }
        return sorted
    }

    fun isProbableDuplicate(book1: Book, book2: Book): Boolean {
        return titlesAreSimilar(book1.title, book2.title) && authorIsTheSame(book1.author, book2.author)
    }

    fun titlesAreSimilar(title1: String, title2: String): Boolean {
        val score: Double = getSimilarityScore(title1, title2)
        return score > 0.8
    }

    fun authorIsTheSame(author1: String, author2: String): Boolean {
        val score: Double = getSimilarityScore(author1, author2)
        return score > 0.9
    }

    private fun getSimilarityScore(string1: String, string2: String): Double {
        val strategy = JaroWinklerStrategy()
        val service = StringSimilarityServiceImpl(strategy)
        return service.score(string1, string2)
    }
}
