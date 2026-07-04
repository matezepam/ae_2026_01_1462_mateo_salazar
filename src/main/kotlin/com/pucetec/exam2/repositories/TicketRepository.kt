package com.pucetec.exam2.repositories
import com.pucetec.exam2.entities.TicketEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<TicketEntity, Long> {

    fun findByLicensePlate(licensePlate: String): List<TicketEntity>

    fun findByIsClosed(isClosed: Boolean): List<TicketEntity>

    fun findByParkingSpaceId(parkingSpaceId: Long): List<TicketEntity>

    fun findByLicensePlateAndIsClosed(licensePlate: String, isClosed: Boolean): List<TicketEntity>
}
