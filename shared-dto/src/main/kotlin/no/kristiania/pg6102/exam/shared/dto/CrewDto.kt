package no.kristiania.pg6102.exam.shared.dto

import io.swagger.annotations.ApiModelProperty

data class CrewDto (

    @get:ApiModelProperty("Id of the crew")
    var crewId: String? = null,

    @get:ApiModelProperty("Name of the crew")
    var crewName: String? = null,

    @get:ApiModelProperty("Crew size; amount of people in the crew")
    var crewSize: Int? = null
)