package com.pucetec.exam2.services

import com.pucetec.exam2.dto.ParkingEntryRequest
import com.pucetec.exam2.dto.ParkingExitRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.TicketEntity
import com.pucetec.exam2.exceptions.ParkingFullException
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceOccupiedException
import com.pucetec.exam2.exceptions.TicketAlreadyClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.mappers.TicketMapper
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val parkingSpaceRepository: ParkingSpaceRepository
) {
    private val logger = LoggerFactory.getLogger(TicketService::class.java)
    private val capacidad = 20

    fun registerEntry(request: ParkingEntryRequest): TicketResponse {
        logger.info("Registering entry for plate {}", request.licensePlate)

        if (parkingSpaceRepository.findByIsOccupied(true).size >= capacidad) {
            throw ParkingFullException("Parking is full")
        }

        val space = parkingSpaceRepository.findByCode(request.parkingSpaceCode)
            ?: throw SpaceNotFoundException("Space with code ${request.parkingSpaceCode} not found")

        if (space.isOccupied) {
            throw SpaceOccupiedException("Space ${request.parkingSpaceCode} is already occupied")
        }

        val updatedSpace = space.copy(isOccupied = true)
        parkingSpaceRepository.save(updatedSpace)

        val ticket = TicketEntity(
            licensePlate = request.licensePlate,
            parkingSpace = updatedSpace
        )

        val saved = ticketRepository.save(ticket)

        return TicketMapper.toDto(saved)
    }

    fun registerExit(request: ParkingExitRequest): TicketResponse {
        logger.info("Registering exit for ticket {}", request.ticketId)

        val ticket = ticketRepository.findById(request.ticketId)
            .orElseThrow { TicketNotFoundException("Ticket not found") }

        if (ticket.isClosed) {
            throw TicketAlreadyClosedException("Ticket already closed")
        }

        val exitTime = LocalDateTime.now()

        val closedTicket = ticket.copy(
            exitTime = exitTime,
            isClosed = true
        )

        val updated = ticketRepository.save(closedTicket)

        val space = updated.parkingSpace.copy(isOccupied = false)
        parkingSpaceRepository.save(space)

        return TicketMapper.toDto(updated)
    }

    fun getAllTickets(): List<TicketResponse> {
        return ticketRepository.findAll().map { TicketMapper.toDto(it) }
    }

    fun getTicketById(id: Long): TicketResponse {
        val ticket = ticketRepository.findById(id)
            .orElseThrow { TicketNotFoundException("Ticket not found") }

        return TicketMapper.toDto(ticket)
    }
}
