package com.pancaldim.bookwishlistapi.controller

import com.pancaldim.bookwishlistapi.service.MergeListsService
import com.pancaldim.bookwishlistapi.service.Secret
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class WishListController {

    @Autowired
    lateinit var apiKey: Secret

    @RequestMapping(value = ["/get-list"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getTechBooks(): ResponseEntity<Any> {
        val scrapeList = MergeListsService().getMergedList(apiKey)
        return ResponseEntity.ok(scrapeList)
    }
}