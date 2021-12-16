package no.kristiania.pg6102.exam.boat.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class BoatDto (

    var boatId: Long? = null,

    var name: String? = null,

    var minCrewSize: Int? = null,

    var maxCrewSize: Int? = null
)