package no.kristiania.pg6102.exam.shared.dto

data class BoatDto (

    var boatId: Long? = null,

    var name: String? = null,

    var minCrewSize: Int? = null,

    var maxCrewSize: Int? = null
)