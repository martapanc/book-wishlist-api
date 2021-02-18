package com.pancaldim.bookwishlistapi.service

import com.pancaldim.bookwishlistapi.model.Book
import org.springframework.stereotype.Service

@Service
class MergeListsService {

    fun getMergedList(apiKey: Secret): List<Book> {
        val amazonService = AmazonService()
        val techBooks = amazonService.scrapeList(WishList.Tech)
        val fictionBooks = amazonService.scrapeList(WishList.Fiction)
        val nonFictionBooks = amazonService.scrapeList(WishList.NonFiction)
        val goodreadsBooks = GoodreadsService().retrieveToReadList(apiKey)

        val mergedList = techBooks + fictionBooks + nonFictionBooks + goodreadsBooks
        return mergedList.sortedBy { it.title }
    }
}