package no.kristiania.pg6102.exam.crew

import no.kristiania.pg6102.exam.crew.db.CrewService
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/api/crew"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class CrewRestApi(
    private val crewService: CrewService
) {

    @GetMapping
    fun getCrews(): ResponseEntity<WrappedResponse<List<CrewDto>>> {
        return RestResponseFactory.payload(
            200,
            crewService.getAllCrews().map { CrewDtoConverter.transform(it) }
        )
    }

    @GetMapping(path = ["/{crewId}"])
    fun getCrewById(
        @PathVariable("crewId") crewId: String
    ): ResponseEntity<WrappedResponse<CrewDto>> {
        val crew = crewService.findCrewByIdEager(crewId)
        if(crew == null) {
            return ResponseEntity.notFound().build()
        }

        return RestResponseFactory.payload(200, CrewDtoConverter.transform(crew))
    }

}
