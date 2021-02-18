package com.pancaldim.bookwishlistapi.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.pancaldim.bookwishlistapi.model.Book
import com.pancaldim.bookwishlistapi.model.Source

@Component
class Secret {

    @Value("\${GOODREADS_KEY}")
    lateinit var apiKey: String
}

class GoodreadsService {

    fun retrieveToReadList(secret: Secret): List<Book> {
        val url = URL("https://www.goodreads.com/review/list?v=2&id=64587022&key=${secret.apiKey}&shelf=to-read&per_page=200")
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"

        val toReadList = mutableListOf<Book>()

        if (con.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val content = StringBuffer()
            while (reader.readLine().also { inputLine = it } != null) {
                content.append(inputLine)
            }
            reader.close()

            val xmlMapper = XmlMapper()
            val node: JsonNode = xmlMapper.readTree(content.toString())

            val jsonMapper = ObjectMapper()
            val json = jsonMapper.writeValueAsString(node)

            val toReadListRaw = jsonMapper.readTree(json)["reviews"]

            for (review in toReadListRaw["review"]) {
                val bookData = review["book"]
                val title = bookData["title"].textValue()
                val author = bookData["authors"]["author"]["name"].textValue()
                val coverUrl = bookData["image_url"].textValue()
                val isbn13 = bookData["isbn13"].textValue()
                toReadList.add(Book(title, author, coverUrl, isbn13, listOf(WishList.Fiction.name), Source.Goodreads))
            }
        }
        con.disconnect()

        return toReadList
    }
}