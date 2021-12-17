package no.kristiania.pg6102.exam.boat

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.kristiania.pg6102.exam.boat.BoatDtoConverter.transform
import no.kristiania.pg6102.exam.boat.db.BoatService
import no.kristiania.pg6102.exam.shared.dto.BoatDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import no.kristiania.pg6102.exam.rest.dto.RestResponseFactory
import no.kristiania.pg6102.exam.rest.dto.WrappedResponse

@Api(value = "/api/boat", description = "Serves boat data")
@RestController
@RequestMapping(path = ["/api/boat"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class BoatRestApi(
    private val boatService: BoatService
) {

    /*
    * Keyset pagination would not be useful here, because of the use-cases
    * that this endpoint would have. This endpoint would be used to
    * get an overview and possibly select boats for trips. Keyset pagination
    * would make it difficult in such cases, since it would prevent the user
    * to navigate to a specific entity without having read all previous pages.
    */
    @ApiOperation("Retrieve data from all saved boats")
    @GetMapping
    fun getBoats(): ResponseEntity<WrappedResponse<List<BoatDto>>> {

        return RestResponseFactory.payload(
            200,
            boatService.getAllBoats().map { BoatDtoConverter.transform(it) }
        )
    }

    @ApiOperation("Retrieve a boat entity based on ID")
    @GetMapping(path = ["/{boatId}"])
    fun getBoatById(
        @PathVariable("boatId") boatId: Long
    ): ResponseEntity<WrappedResponse<BoatDto>> {
        val boat = boatService.findBoatByIdEager(boatId)
        if(boat == null) {
            return ResponseEntity.notFound().build()
        }

        return RestResponseFactory.payload(200, BoatDtoConverter.transform(boat))
    }

    @ApiOperation("Returns all boats that are suitable for a certain number of crew members")
    @GetMapping(path = ["/crewSize/{crewSize}"])
    fun getBoatWithinCrewRange(
        @ApiParam("Crew size that the potential boats have to room")
        @PathVariable("crewSize")
        crewSize: Int
    ): ResponseEntity<WrappedResponse<List<BoatDto>>> {

        val boatList: List<BoatDto>
        try {
             boatList = boatService.getBoatsWithinRange(crewSize).map { transform(it) }
        } catch (ex: IllegalArgumentException) {
            return RestResponseFactory.userFailure(ex.message!!)
        }

        return RestResponseFactory.payload(200, boatList)
    }

}
