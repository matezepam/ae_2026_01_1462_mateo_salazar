package com.pucetec.exam2.repositories
import com.pucetec.exam2.entities.ParkingSpaceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpaceRepository : JpaRepository<ParkingSpaceEntity, Long> {

    fun findByCode(code: String): ParkingSpaceEntity?

    fun findByIsOccupied(isOccupied: Boolean): List<ParkingSpaceEntity>
}
