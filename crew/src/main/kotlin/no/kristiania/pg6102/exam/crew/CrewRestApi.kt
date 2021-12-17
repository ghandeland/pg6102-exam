package no.kristiania.pg6102.exam.crew

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.kristiania.pg6102.exam.crew.CrewDtoConverter.transform
import no.kristiania.pg6102.exam.crew.db.CrewService
import no.kristiania.pg6102.exam.rest.dto.PageDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import no.kristiania.pg6102.exam.rest.dto.RestResponseFactory
import no.kristiania.pg6102.exam.rest.dto.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "/api/crew", description = "Servers data related to crews, with pagination")
@RestController
@RequestMapping(path = ["/api/crew"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class CrewRestApi(
    private val crewService: CrewService
) {
    @ApiOperation("Returns all crews")
    @GetMapping
    fun getCrews(): ResponseEntity<WrappedResponse<List<CrewDto>>> {
        return RestResponseFactory.payload(
            200,
            crewService.getAllCrews().map { CrewDtoConverter.transform(it) }
        )
    }

    /*
    * This code in this function has been adapted from the keyset
    * functionality introduced in lecture 4 in the course repository:
    * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-04/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/RestApi.kt
     */

    /*
    * I additionally wrote this endpoint for the sake of implementing
    * pagination. It made most sense to choose the crew entity, since
    * the crewSize property could be used as a pagination value.
    * */
    @ApiOperation("Return an iterable page of crew entities, sorted on crewSize descending")
    @GetMapping("/pagination")
    fun getCrewsPagination(
        @ApiParam("Id of crew in the previous page")
        @RequestParam("keysetId", required = false)
        keysetId: String?,

        @ApiParam("Size of crew in the previous page")
        @RequestParam("keysetCrewSize", required = false)
        keysetCrewSize: Int?
    ): ResponseEntity<WrappedResponse<PageDto<CrewDto>>> {

        val page = PageDto<CrewDto>()

        val n = 10
        val crews = crewService.getNextPage(n, keysetId, keysetCrewSize).map { transform(it) }
        page.list = crews

        if (crews.size == n) {
            val last = crews.last()
            page.next = "/api/crew/pagination?keysetId=${last.crewId}&keysetCrewSize=${last.crewSize}"
        }

        return ResponseEntity
            .status(200)
            .body(WrappedResponse(200, page).validated())
    }

    @ApiOperation("Returns a crew based on crew ID")
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
