package com.pucetec.exam2.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
data class TicketEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val licensePlate: String,

    @Column(nullable = false)
    val entryTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val exitTime: LocalDateTime? = null,

    @Column(nullable = false)
    val isClosed: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_space_id", nullable = false)
    val parkingSpace: ParkingSpaceEntity
)
