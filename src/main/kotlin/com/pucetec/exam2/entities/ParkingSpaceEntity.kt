package com.pucetec.exam2.entities

import jakarta.persistence.*

@Entity
@Table(name = "parking_spaces")
data class ParkingSpaceEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val code: String,

    @Column(nullable = false)
    val isOccupied: Boolean = false
)