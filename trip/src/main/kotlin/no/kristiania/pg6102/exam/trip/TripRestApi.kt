package no.kristiania.pg6102.exam.trip

import no.kristiania.pg6102.exam.shared.dto.PortDto
import no.kristiania.pg6102.exam.trip.db.TripService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/api/trip"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class TripRestApi(
    private val tripService: TripService
) {

    @GetMapping
    fun getPortsTemp(): ResponseEntity<WrappedResponse<List<PortDto>>> {
        try {
            return RestResponseFactory.payload(200, tripService.fetchPortTemp())
        } catch(e: Exception) {
            return RestResponseFactory.notFound("feil")
        }
    }


    /*@GetMapping
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
    }*/

}
