package no.kristiania.pg6102.exam.port

import no.kristiania.pg6102.exam.port.db.Port
import no.kristiania.pg6102.exam.shared.dto.PortDto

object PortDtoConverter {
    fun transform(port: Port): PortDto {
        return port.run { PortDto(portId, name, city, country) }
    }

    fun transform(dto: PortDto): Port {
        return Port().apply {
            portId = dto.portId
            name = dto.name
            city = dto.city
            country = dto.country
        }
    }
}