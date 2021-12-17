package no.kristiania.pg6102.exam.boat

import no.kristiania.pg6102.exam.boat.BoatDtoConverter.transform
import no.kristiania.pg6102.exam.boat.db.BoatService
import no.kristiania.pg6102.exam.shared.dto.BoatDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/api/boat"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class BoatRestApi(
    private val boatService: BoatService
) {

    @GetMapping
    fun getBoats(): ResponseEntity<WrappedResponse<List<BoatDto>>> {
        return RestResponseFactory.payload(
            200,
            boatService.getAllBoats().map { BoatDtoConverter.transform(it) }
        )
    }

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

    // Returns all boats that are suitable for a certain number of crew members
    @GetMapping(path = ["/crewSize/{crewSize}"])
    fun getBoatWithinCrewRange(
        @PathVariable("crewSize") crewSize: Int
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
