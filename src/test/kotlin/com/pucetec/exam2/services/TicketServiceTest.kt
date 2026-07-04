package com.pucetec.exam2.services

import com.pucetec.exam2.dto.ParkingEntryRequest
import com.pucetec.exam2.dto.ParkingExitRequest
import com.pucetec.exam2.entities.ParkingSpaceEntity
import com.pucetec.exam2.entities.TicketEntity
import com.pucetec.exam2.exceptions.ParkingFullException
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceOccupiedException
import com.pucetec.exam2.exceptions.TicketAlreadyClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import java.util.Optional

class TicketServiceTest {
    private lateinit var ticketRepository: TicketRepository
    private lateinit var parkingSpaceRepository: ParkingSpaceRepository
    private lateinit var service: TicketService

    @BeforeEach
    fun setUp() {
        ticketRepository = Mockito.mock(TicketRepository::class.java)
        parkingSpaceRepository = Mockito.mock(ParkingSpaceRepository::class.java)
        service = TicketService(ticketRepository, parkingSpaceRepository)
    }

    @Test
    fun `register entry creates ticket and occupies space`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = false)

        Mockito.`when`(parkingSpaceRepository.findByIsOccupied(true)).thenReturn(emptyList())
        Mockito.`when`(parkingSpaceRepository.findByCode("A1")).thenReturn(space)
        Mockito.`when`(parkingSpaceRepository.save(any(ParkingSpaceEntity::class.java))).thenAnswer { it.arguments[0] }
        Mockito.`when`(ticketRepository.save(any(TicketEntity::class.java))).thenAnswer {
            (it.arguments[0] as TicketEntity).copy(id = 10)
        }

        val response = service.registerEntry(ParkingEntryRequest("ABC123", "A1"))

        assertEquals(10, response.id)
        assertEquals("ABC123", response.licensePlate)
        assertEquals("A1", response.parkingSpaceCode)
        Mockito.verify(parkingSpaceRepository).save(space.copy(isOccupied = true))
    }

    @Test
    fun `register entry fails when parking is full`() {
        val occupiedSpaces = (1..20).map { ParkingSpaceEntity(id = it.toLong(), code = "A$it", isOccupied = true) }
        Mockito.`when`(parkingSpaceRepository.findByIsOccupied(true)).thenReturn(occupiedSpaces)

        val exception = assertThrows(ParkingFullException::class.java) {
            service.registerEntry(ParkingEntryRequest("ABC123", "A1"))
        }

        assertEquals("Parking is full", exception.message)
    }

    @Test
    fun `register entry fails when space does not exist`() {
        Mockito.`when`(parkingSpaceRepository.findByIsOccupied(true)).thenReturn(emptyList())
        Mockito.`when`(parkingSpaceRepository.findByCode("Z9")).thenReturn(null)

        val exception = assertThrows(SpaceNotFoundException::class.java) {
            service.registerEntry(ParkingEntryRequest("ABC123", "Z9"))
        }

        assertTrue(exception.message!!.contains("Z9"))
    }

    @Test
    fun `register entry fails when space is occupied`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = true)
        Mockito.`when`(parkingSpaceRepository.findByIsOccupied(true)).thenReturn(listOf(space))
        Mockito.`when`(parkingSpaceRepository.findByCode("A1")).thenReturn(space)

        val exception = assertThrows(SpaceOccupiedException::class.java) {
            service.registerEntry(ParkingEntryRequest("ABC123", "A1"))
        }

        assertTrue(exception.message!!.contains("A1"))
    }

    @Test
    fun `register exit closes ticket and frees space`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = true)
        val ticket = TicketEntity(id = 7, licensePlate = "ABC123", parkingSpace = space)

        Mockito.`when`(ticketRepository.findById(7)).thenReturn(Optional.of(ticket))
        Mockito.`when`(ticketRepository.save(any(TicketEntity::class.java))).thenAnswer { it.arguments[0] }
        Mockito.`when`(parkingSpaceRepository.save(any(ParkingSpaceEntity::class.java))).thenAnswer { it.arguments[0] }

        val response = service.registerExit(ParkingExitRequest(7))

        assertEquals(7, response.id)
        assertTrue(response.isClosed)
        assertNotNull(response.exitTime)
        Mockito.verify(parkingSpaceRepository).save(space.copy(isOccupied = false))
    }

    @Test
    fun `register exit fails when ticket does not exist`() {
        Mockito.`when`(ticketRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(TicketNotFoundException::class.java) {
            service.registerExit(ParkingExitRequest(99))
        }

        assertEquals("Ticket not found", exception.message)
    }

    @Test
    fun `register exit fails when ticket is already closed`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = false)
        val ticket = TicketEntity(id = 7, licensePlate = "ABC123", isClosed = true, parkingSpace = space)
        Mockito.`when`(ticketRepository.findById(7)).thenReturn(Optional.of(ticket))

        val exception = assertThrows(TicketAlreadyClosedException::class.java) {
            service.registerExit(ParkingExitRequest(7))
        }

        assertEquals("Ticket already closed", exception.message)
    }

    @Test
    fun `get all tickets maps responses`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = true)
        Mockito.`when`(ticketRepository.findAll()).thenReturn(listOf(TicketEntity(id = 1, licensePlate = "ABC123", parkingSpace = space)))

        val response = service.getAllTickets()

        assertEquals(1, response.size)
        assertEquals("ABC123", response.first().licensePlate)
    }

    @Test
    fun `get ticket by id returns response`() {
        val space = ParkingSpaceEntity(id = 1, code = "A1", isOccupied = true)
        Mockito.`when`(ticketRepository.findById(1)).thenReturn(Optional.of(TicketEntity(id = 1, licensePlate = "ABC123", parkingSpace = space)))

        val response = service.getTicketById(1)

        assertFalse(response.isClosed)
        assertEquals("A1", response.parkingSpaceCode)
    }

    @Test
    fun `get ticket by id fails when ticket does not exist`() {
        Mockito.`when`(ticketRepository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(TicketNotFoundException::class.java) {
            service.getTicketById(1)
        }

        assertEquals("Ticket not found", exception.message)
    }
}
