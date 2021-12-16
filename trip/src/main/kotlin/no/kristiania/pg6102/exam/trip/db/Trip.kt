package no.kristiania.pg6102.exam.trip.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Trip {

    @get:Id
    @get:GeneratedValue
    var tripId: Long? = null

    @get:NotNull
    var originPortId: Long? = null

    @get:NotNull
    var destinationPortId: Long? = null

    @get:NotNull
    var boatId: Long? = null

    @get:NotBlank
    var crewId: String? = null
}