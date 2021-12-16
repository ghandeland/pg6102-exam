package no.kristiania.pg6102.exam.trip.dto

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TripDto(

    var tripId: Long? = null,

    var originPortId: Long? = null,

    var destinationPortId: Long? = null,

    var boatId: Long? = null,

    var crewId: String? = null
)