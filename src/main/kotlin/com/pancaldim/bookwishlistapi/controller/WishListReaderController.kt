package com.pancaldim.bookwishlistapi.controller

import com.pancaldim.bookwishlistapi.exception.PrivateListException
import com.pancaldim.bookwishlistapi.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class WishListReaderController {

    @Autowired
    lateinit var apiSecret: Secret

    @Autowired
    lateinit var settings: Settings

    val service = AmazonService()

    @RequestMapping(value = ["/tech"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getTechBooks(): ResponseEntity<Any> {
        return try {
            val scrapeList = service.scrapeListFromPrintTemplate(WishList.Tech)
            ResponseEntity.ok(scrapeList)
        } catch (e: PrivateListException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        }
    }

    @RequestMapping(value = ["/non-fiction"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getNonFictionBooks(): ResponseEntity<Any> {
        return try {
            val scrapeList = service.scrapeListFromPrintTemplate(WishList.NonFiction)
            ResponseEntity.ok(scrapeList)
        } catch (e: PrivateListException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        }
    }

    @RequestMapping(value = ["/fiction"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getFictionBooks(): ResponseEntity<Any> {
        return try {
            val scrapeList = service.scrapeListFromPrintTemplate(WishList.Fiction)
            ResponseEntity.ok(scrapeList)
        } catch (e: PrivateListException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        }
    }

    @RequestMapping(value = ["/fiction-2"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getFictionBooks2(): ResponseEntity<Any> {
        return try {
            val scrapeList = service.scrapeList(settings, WishList.Fiction)
            ResponseEntity.ok(scrapeList)
        } catch (e: PrivateListException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        }
    }

    @RequestMapping(value = ["/goodreads"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getGoodreadsToReadBooks(): ResponseEntity<Any> {
        val toReadList = GoodreadsService().retrieveToReadList(apiSecret)
        return ResponseEntity.ok(toReadList)
    }

    @RequestMapping(value = ["/oauth"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getOauth(): ResponseEntity<Any> {
        val oauthResponse = GoodreadsService().oauthService(apiSecret)
        return ResponseEntity.ok(oauthResponse)
    }
}