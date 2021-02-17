package com.pancaldim.bookwishlistapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class HealthController {

    @RequestMapping(value = ["/health"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getHealthStatus(): Status {
        return getStatus()
    }

    @RequestMapping(value = ["/"], produces = ["application/json"], method = [RequestMethod.GET])
    fun getHomePage(): Status {
        return getStatus()
    }

    private fun getStatus() = Status(Date(), "UP")
}

data class Status(val date: Date, val status: String)