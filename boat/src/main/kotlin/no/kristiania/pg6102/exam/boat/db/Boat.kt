package no.kristiania.pg6102.exam.boat.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Boat {

    @get:Id @get:GeneratedValue
    var boatId: Long? = null

    @get:NotBlank
    var name: String? = null

    @get:NotNull @get:Min(1)
    var minCrewSize: Int? = null

    @get:NotNull @get:Min(1)
    var maxCrewSize: Int? = null
}