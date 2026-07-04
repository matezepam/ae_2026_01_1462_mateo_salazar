package com.pucetec.exam2.config

import com.pucetec.exam2.entities.ParkingSpaceEntity
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ParkingDataInitializer {
    private val logger = LoggerFactory.getLogger(ParkingDataInitializer::class.java)

    @Bean
    fun seedParkingSpaces(repository: ParkingSpaceRepository): CommandLineRunner {
        return CommandLineRunner {
            if (repository.count() == 0L) {
                val spaces = (1..20).map { ParkingSpaceEntity(code = "A$it") }
                repository.saveAll(spaces)
                logger.info("Seeded {} parking spaces", spaces.size)
            }
        }
    }
}
