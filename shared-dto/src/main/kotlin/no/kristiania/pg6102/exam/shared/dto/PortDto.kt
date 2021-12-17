package no.kristiania.pg6102.exam.shared.dto

import io.swagger.annotations.ApiModelProperty

data class PortDto(

    @get:ApiModelProperty("ID of the port")
    var portId: Long? = null,

    @get:ApiModelProperty("Name of the port")
    var name: String? = null,

    @get:ApiModelProperty("The city that the port resides in")
    var city: String? = null,

    @get:ApiModelProperty("The country that the port resides in")
    var country: String? = null
)