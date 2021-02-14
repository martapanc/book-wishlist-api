package com.pancaldim.bookwishlistapi.controller

import com.pancaldim.bookwishlistapi.model.Book
import com.pancaldim.bookwishlistapi.service.WishListService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WishListController {

    @GetMapping("/tech-books")
    fun getTechBooks(): List<Book> {
        val service = WishListService()
        return service.crawlTechList()
    }
}