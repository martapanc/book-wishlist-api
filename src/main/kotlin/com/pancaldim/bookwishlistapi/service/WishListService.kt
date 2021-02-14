package com.pancaldim.bookwishlistapi.service

import com.pancaldim.bookwishlistapi.model.Book
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class WishListService {

    fun crawlTechList(): List<Book> {
        val document = Jsoup.connect("https://www.amazon.co.uk/hz/wishlist/printview/184OAYYG9NB37").get()
        val table: Element = document.select("table")[0]
        val rows: Elements = table.select("tr")
        val bookList = mutableListOf<Book>()

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

            val book = Book(title, author, coauthors, coverUrl)
            bookList.add(book)
        }

        return bookList
    }
}