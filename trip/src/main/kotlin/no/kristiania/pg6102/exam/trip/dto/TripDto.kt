package no.kristiania.pg6102.exam.trip.dto

import io.swagger.annotations.ApiModelProperty
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TripDto(

    @get:ApiModelProperty("ID of the trip")
    var tripId: Long? = null,

    @get:ApiModelProperty("ID of the origin port of the trip")
    var originPortId: Long? = null,

    @get:ApiModelProperty("ID of the destination port of the trip")
    var destinationPortId: Long? = null,

    @get:ApiModelProperty("ID of the boat taking the trip")
    var boatId: Long? = null,

    @get:ApiModelProperty("ID of the crew taking in the trip")
    var crewId: String? = null
)