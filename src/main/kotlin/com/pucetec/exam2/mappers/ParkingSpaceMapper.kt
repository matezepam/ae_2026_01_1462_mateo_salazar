package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.entities.ParkingSpaceEntity

object ParkingSpaceMapper {

    fun toDto(entity: ParkingSpaceEntity): ParkingSpaceResponse {
        return ParkingSpaceResponse(
            id = entity.id,
            code = entity.code,
            isOccupied = entity.isOccupied
        )
    }

    fun toEntity(dto: ParkingSpaceResponse): ParkingSpaceEntity {
        return ParkingSpaceEntity(
            id = dto.id ?: 0,
            code = dto.code,
            isOccupied = dto.isOccupied
        )
    }
}
