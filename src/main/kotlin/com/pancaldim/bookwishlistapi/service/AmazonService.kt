package com.pancaldim.bookwishlistapi.service

import com.pancaldim.bookwishlistapi.exception.PrivateListException
import com.pancaldim.bookwishlistapi.model.Book
import com.pancaldim.bookwishlistapi.model.Source
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

class AmazonService {

    private val baseUrl = "https://www.amazon.co.uk/hz/wishlist"

    fun scrapeList(wishList: WishList): List<Book> {
        val url = "${baseUrl}/genericItemsPage/${wishList.listId}"
        val bookList = mutableListOf<Book>()

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        val driver: WebDriver = ChromeDriver()
        driver.manage().window().maximize()
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)

        driver.get(url)
        val jse = driver as JavascriptExecutor
        var lastHeight: Int = Integer.parseInt(jse.executeScript("return document.body.scrollHeight").toString())
        var newHeight: Int

        while (true) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight);")
            newHeight = Integer.parseInt(jse.executeScript("return document.body.scrollHeight").toString())
            if (newHeight == lastHeight) {
                break
            }
            lastHeight = newHeight
        }

        Thread.sleep(1500)
        val listUl: WebElement = driver.findElement(By.xpath("//ul[@id='g-items']"))
        val bookElements = listUl.findElements(By.tagName("li"))
        for (element in bookElements) {
            println(element)
        }

        driver.quit()
        return bookList
    }

    fun scrapeListFromPrintTemplate(wishList: WishList): List<Book> {
        val document = Jsoup.connect("${baseUrl}/printview/${wishList.listId}").get()
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