package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.ParkingEntryRequest
import com.pucetec.exam2.dto.ParkingExitRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.services.TicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tickets")
class TicketController(
    private val ticketService: TicketService
) {

    @PostMapping("/entry")
    fun registerEntry(@RequestBody request: ParkingEntryRequest): ResponseEntity<TicketResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.registerEntry(request))
    }

    @PostMapping("/exit")
    fun registerExit(@RequestBody request: ParkingExitRequest): ResponseEntity<TicketResponse> {
        return ResponseEntity.ok(ticketService.registerExit(request))
    }
}
