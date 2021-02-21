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
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Settings {

    @Value("\${CHROMEDRIVER_PATH}")
    lateinit var chromedriverPath: String
}

class AmazonService {

    private val baseUrl = "https://www.amazon.co.uk/hz/wishlist"

    fun scrapeList(settings: Settings, wishList: WishList): List<Book> {
        val url = "${baseUrl}/genericItemsPage/${wishList.listId}"
        val bookList = mutableListOf<Book>()

        System.setProperty("webdriver.chrome.driver", settings.chromedriverPath);
        val driver: WebDriver = ChromeDriver()
        driver.manage().window().maximize()
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.MILLISECONDS)

        driver.get(url)
        val jse = driver as JavascriptExecutor
        while (driver.findElements(By.id("endOfListMarker")).isEmpty()) {
            jse.executeScript("window.scrollBy(0, 300)")
        }

        val listUl: WebElement = driver.findElement(By.xpath("//ul[@id='g-items']"))
        val bookElements = listUl.findElements(By.tagName("li"))
        for (element in bookElements) {
            val coverUrl = element.findElement(By.cssSelector("[id*=itemImage_] > a > img")).getAttribute("src")
            val titleElement = element.findElement(By.cssSelector("[id*=itemName_]"))
            val title = titleElement.getAttribute("title")
            val link = titleElement.getAttribute("href")
            var author = element.findElement(By.cssSelector("[id*=item-byline-]")).text
            author = author
                .substringAfter("by ", "")
                .substringBefore(" (", "")
                .substringBefore(", foreword by")
            bookList.add(Book(title, author, coverUrl, source = Source.Amazon, link = link))
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