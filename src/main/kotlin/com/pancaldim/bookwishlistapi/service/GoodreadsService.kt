package com.pancaldim.bookwishlistapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Component
class Secret {

    @Value("\${GOODREADS_KEY}")
    lateinit var apiKey: String
}

class GoodreadsService {

    fun retrieveToReadList(secret: Secret): String {
        val url = URL("https://www.goodreads.com/review/list?v=2&id=64587022&key=${secret.apiKey}&shelf=to-read&per_page=200")
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        val status = con.responseCode
        val content = StringBuffer()

        if (status == 200) {
            val reader = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            while (reader.readLine().also { inputLine = it } != null) {
                content.append(inputLine)
            }
            reader.close()
        }
        con.disconnect()

        return content.toString()
    }
}