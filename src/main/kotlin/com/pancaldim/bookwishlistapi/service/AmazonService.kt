package com.pancaldim.bookwishlistapi.service

import com.pancaldim.bookwishlistapi.exception.PrivateListException
import com.pancaldim.bookwishlistapi.model.Book
import com.pancaldim.bookwishlistapi.model.Source
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class WishListService {

    fun scrapeList(wishList: WishList): List<Book> {
        val document = Jsoup.connect("https://www.amazon.co.uk/hz/wishlist/printview/${wishList.listId}").get()
        val bookList = mutableListOf<Book>()
        try {
            val table: Element = document.select("table")[0]
            val rows: Elements = table.select("tr")

            for (row in rows.subList(1, rows.size)) {
                val cols = row.select("td")
                val title = cols[1].select("span")[0].text()
                    .substringBefore(" (")
                    .substringBefore(".")
                var author = cols[1].text()
                    .substringAfter("by ")
                    .substringBefore(" (")
                    .substringBefore(", foreword by")
                var coauthors = author.split(", ").toMutableList()
                if (coauthors.size > 1) {
                    author = coauthors[0]
                    coauthors.remove(author)
                } else {
                    coauthors = mutableListOf()
                }

                val coverUrl = cols[0].select("img").attr("src")

                val book = Book(title, author, coverUrl, null, listOf(wishList.name), Source.Amazon)
                bookList.add(book)
            }
        } catch (e: IndexOutOfBoundsException) {
            throw PrivateListException("Error retrieving books -- check that the list is not set to private")
        }

        return bookList
    }
}

enum class WishList(val listId: String) {
    Tech("184OAYYG9NB37"), NonFiction("2K0O50JYPM628"), Fiction("1OK2Q8NZ7P6D3")
}