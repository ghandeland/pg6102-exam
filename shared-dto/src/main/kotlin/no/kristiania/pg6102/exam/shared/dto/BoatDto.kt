package no.kristiania.pg6102.exam.shared.dto

import io.swagger.annotations.ApiModelProperty

data class BoatDto (

    @get:ApiModelProperty("Id of the boat")
    var boatId: Long? = null,

    @get:ApiModelProperty("Name of the boat")
    var name: String? = null,

    @get:ApiModelProperty("Minimum crew size allowed for boat")
    var minCrewSize: Int? = null,

    @get:ApiModelProperty("Maximum crew size allowed for boat boat")
    var maxCrewSize: Int? = null
)