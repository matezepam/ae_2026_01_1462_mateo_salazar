package com.pucetec.exam2.dto

import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val error: String,
    val status: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)