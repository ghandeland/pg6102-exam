package no.kristiania.pg6102.exam.crew

import no.kristiania.pg6102.exam.crew.db.Crew
import no.kristiania.pg6102.exam.crew.dto.CrewDto


object CrewDtoConverter {
    fun transform(crew: Crew): CrewDto {
        return crew.run { CrewDto(crewId, crewName, crewSize) }
    }

    fun transform(dto: CrewDto): Crew {
        return Crew().apply {
            crewId = dto.crewId
            crewName = dto.crewName
            crewSize = dto.crewSize
        }
    }
}