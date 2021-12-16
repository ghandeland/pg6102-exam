package no.kristiania.pg6102.exam.shared.dto

data class PortDto(
    var portId: Long? = null,

    var name: String? = null,

    var city: String? = null,

    var country: String? = null
)