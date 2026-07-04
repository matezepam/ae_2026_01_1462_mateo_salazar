package com.pucetec.exam2.services

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.entities.ParkingSpaceEntity
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.mappers.ParkingSpaceMapper
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParkingSpaceService(
    private val repository: ParkingSpaceRepository
) {
    private val logger = LoggerFactory.getLogger(ParkingSpaceService::class.java)

    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        logger.info("Listing available parking spaces")
        return repository.findByIsOccupied(false).map { ParkingSpaceMapper.toDto(it) }
    }

    fun getAllSpaces(): List<ParkingSpaceResponse> {
        return repository.findAll().map { ParkingSpaceMapper.toDto(it) }
    }

    fun getSpaceById(id: Long): ParkingSpaceResponse {
        val space = repository.findById(id)
            .orElseThrow { SpaceNotFoundException("Space with id $id not found") }

        return ParkingSpaceMapper.toDto(space)
    }

    fun createSpace(dto: ParkingSpaceResponse): ParkingSpaceResponse {
        val saved = repository.save(
            ParkingSpaceEntity(
                code = dto.code,
                isOccupied = dto.isOccupied
            )
        )

        return ParkingSpaceMapper.toDto(saved)
    }

    fun updateSpace(id: Long, dto: ParkingSpaceResponse): ParkingSpaceResponse {
        val existing = repository.findById(id)
            .orElseThrow { SpaceNotFoundException("Space with id $id not found") }

        val updated = existing.copy(
            code = dto.code,
            isOccupied = dto.isOccupied
        )

        return ParkingSpaceMapper.toDto(repository.save(updated))
    }

    fun deleteSpace(id: Long) {
        if (!repository.existsById(id)) {
            throw SpaceNotFoundException("Space with id $id not found")
        }
        repository.deleteById(id)
    }
}
