package com.pancaldim.bookwishlistapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class HealthController {

    @GetMapping("/health")
    fun getHealthStatus(): Status {
        return getStatus()
    }

    @GetMapping("/")
    fun getHomePage(): Status {
        return getStatus()
    }

    private fun getStatus() = Status(Date(), "UP")
}

data class Status(val date: Date, val status: String)