package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.TicketEntity

object TicketMapper {

    fun toDto(entity: TicketEntity): TicketResponse {
        return TicketResponse(
            id = entity.id,
            licensePlate = entity.licensePlate,
            entryTime = entity.entryTime,
            exitTime = entity.exitTime,
            isClosed = entity.isClosed,
            parkingSpaceCode = entity.parkingSpace.code
        )
    }
}
