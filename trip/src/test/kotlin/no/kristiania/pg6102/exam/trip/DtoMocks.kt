package no.kristiania.pg6102.exam.trip

import no.kristiania.pg6102.exam.shared.dto.BoatDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import no.kristiania.pg6102.exam.shared.dto.PortDto

object DtoMocks {
    fun getPort(): PortDto { return PortDto(3, "Port of Stockholm", "Stockholm", "Sweden") }

    fun getCrew(): CrewDto { return CrewDto("c006", "Crew Golf", 125) }

    fun getBoat(): BoatDto { return BoatDto(10, "Submarine", 50, 250) }
}