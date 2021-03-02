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
import com.google.api.client.auth.oauth.*
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.pancaldim.bookwishlistapi.model.Book
import com.pancaldim.bookwishlistapi.model.Source

@Component
class Secret {

    @Value("\${GOODREADS_KEY}")
    lateinit var apiKey: String

    @Value("\${GOODREADS_SECRET}")
    lateinit var apiSecret: String
}

class GoodreadsService {

    fun deleteBookFromWishlist(bookId: String, secret: Secret): String {
        val oauthParameters = getOAuthParameters(secret)
        // Use OAuthParameters to access the desired Resource URL
        val requestFactory = ApacheHttpTransport().createRequestFactory(oauthParameters)
        val genericUrl = GenericUrl("https://www.goodreads.com/review/destroy/${bookId}?format=xml")
        val resp = requestFactory.buildDeleteRequest(genericUrl).execute()
        return resp.parseAsString()
    }

    private fun getOAuthParameters(secret: Secret): OAuthParameters {
        val baseUrl = "https://www.goodreads.com"
        val tokenServerUrl = "$baseUrl/oauth/request_token"
        val authenticateUrl = "$baseUrl/oauth/authorize"
        val accessTokenUrl = "$baseUrl/oauth/access_token"

        val signer = OAuthHmacSigner()
        // Get Temporary Token
        val getTemporaryToken = OAuthGetTemporaryToken(tokenServerUrl)
        signer.clientSharedSecret = secret.apiSecret
        getTemporaryToken.signer = signer
        getTemporaryToken.consumerKey = secret.apiKey
        getTemporaryToken.transport = NetHttpTransport()
        val temporaryTokenResponse = getTemporaryToken.execute()

        // Build Authenticate URL
        val accessTempToken = OAuthAuthorizeTemporaryTokenUrl(authenticateUrl)
        accessTempToken.temporaryToken = temporaryTokenResponse.token
        val authUrl = accessTempToken.build()

        // Redirect to Authenticate URL in order to get Verifier Code
        println("Goodreads oAuth sample: Please visit the following URL to authorize:")
        println(authUrl)
        println("Waiting 10s to allow time for visiting auth URL and authorizing...")
        Thread.sleep(10000)

        println("Waiting time complete - assuming access granted and attempting to get access token")
        // Get Access Token using Temporary token and Verifier Code
        val getAccessToken = OAuthGetAccessToken(accessTokenUrl)
        getAccessToken.signer = signer
        // NOTE: This is the main difference from the StackOverflow example
        signer.tokenSharedSecret = temporaryTokenResponse.tokenSecret
        getAccessToken.temporaryToken = temporaryTokenResponse.token
        getAccessToken.transport = NetHttpTransport()
        getAccessToken.consumerKey = secret.apiKey
        val accessTokenResponse = getAccessToken.execute()

        // Build OAuthParameters in order to use them while accessing the resource
        val oauthParameters = OAuthParameters()
        signer.tokenSharedSecret = accessTokenResponse.tokenSecret
        oauthParameters.signer = signer
        oauthParameters.consumerKey = secret.apiKey
        oauthParameters.token = accessTokenResponse.token
        return oauthParameters
    }

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