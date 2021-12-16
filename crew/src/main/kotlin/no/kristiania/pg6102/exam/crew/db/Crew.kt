package no.kristiania.pg6102.exam.crew.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Crew {
    @get:Id
    var crewId: String? = null

    @get:NotBlank
    var crewName: String? = null

    @get:NotNull @Min(1)
    var crewSize: Int? = null
}