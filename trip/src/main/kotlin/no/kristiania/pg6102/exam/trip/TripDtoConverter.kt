package no.kristiania.pg6102.exam.trip

import no.kristiania.pg6102.exam.trip.db.Trip
import no.kristiania.pg6102.exam.trip.dto.TripDto


object TripDtoConverter {
    fun transform(trip: Trip): TripDto {
        return trip.run { TripDto(tripId, originPortId, destinationPortId, boatId, crewId) }
    }

    fun transform(dto: TripDto): Trip {
        return Trip().apply {
            tripId = dto.tripId
            originPortId = dto.originPortId
            destinationPortId = dto.destinationPortId
            boatId = dto.boatId
            crewId = dto.crewId
        }
    }
}