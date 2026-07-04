package com.pucetec.exam2.exceptions

import com.pucetec.exam2.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ParkingFullException::class)
    fun handleParkingFull(ex: ParkingFullException): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message!!, "PARKING_FULL", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleSpaceNotFound(ex: SpaceNotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message!!, "SPACE_NOT_FOUND", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(SpaceOccupiedException::class)
    fun handleSpaceOccupied(ex: SpaceOccupiedException): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message!!, "SPACE_OCCUPIED", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(ex: TicketNotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message!!, "TICKET_NOT_FOUND", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(TicketAlreadyClosedException::class)
    fun handleTicketClosed(ex: TicketAlreadyClosedException): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message!!, "TICKET_ALREADY_CLOSED", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        return buildResponse(ex.message ?: "Unexpected error", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun buildResponse(message: String, error: String, status: HttpStatus): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                message = message,
                error = error,
                status = status.value(),
                timestamp = LocalDateTime.now()
            ),
            status
        )
    }
}
