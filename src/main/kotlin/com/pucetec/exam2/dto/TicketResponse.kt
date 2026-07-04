package com.pucetec.exam2.dto
import java.time.LocalDateTime

data class TicketResponse(
    val id: Long?,
    val licensePlate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val isClosed: Boolean,
    val parkingSpaceCode: String
)
