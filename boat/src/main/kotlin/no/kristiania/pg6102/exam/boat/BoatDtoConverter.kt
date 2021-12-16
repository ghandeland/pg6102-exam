package no.kristiania.pg6102.exam.boat

import no.kristiania.pg6102.exam.boat.db.Boat
import no.kristiania.pg6102.exam.boat.dto.BoatDto


object BoatDtoConverter {
    fun transform(boat: Boat): BoatDto {
        return boat.run { BoatDto(boatId, name, minCrewSize, maxCrewSize) }
    }

    fun transform(dto: BoatDto): Boat {
        return Boat().apply {
            boatId = dto.boatId
            name = dto.name
            minCrewSize = dto.minCrewSize
            maxCrewSize = dto.maxCrewSize
        }
    }
}