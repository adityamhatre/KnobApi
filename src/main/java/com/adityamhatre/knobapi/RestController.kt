package com.adityamhatre.knobapi

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/knobapi")
class RestController {
    @RequestMapping("/lock")
    fun lock() = "Locked"

    @RequestMapping("/unlock")
    fun unlock() = "Unlocked"
}