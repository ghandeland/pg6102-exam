package no.kristiania.pg6102.exam.trip

import no.kristiania.pg6102.exam.shared.dto.BoatDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import no.kristiania.pg6102.exam.shared.dto.PortDto
import no.kristiania.pg6102.exam.trip.TripDtoConverter.transform
import no.kristiania.pg6102.exam.trip.db.TripService
import no.kristiania.pg6102.exam.trip.dto.TripDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/api/trip"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class TripRestApi(
    private val tripService: TripService
) {

    @GetMapping
    fun getTrips(): ResponseEntity<WrappedResponse<List<TripDto>>> {
        return RestResponseFactory.payload(
            200,
            tripService.getAllTrips().map { transform(it) }
        )
    }

    @GetMapping(path = ["/{tripId}"])
    fun getTripById(@PathVariable("tripId") tripId: Long): ResponseEntity<WrappedResponse<TripDto>> {
        val trip = tripService.findByTripIdEager(tripId)
        return if(trip !== null) {
            RestResponseFactory.payload(
                200,
                transform(trip)
            )
        } else {
            RestResponseFactory.notFound<TripDto>("Could not find trip with given id $tripId")
        }
    }

    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun postTrip(@RequestBody dto: TripDto): ResponseEntity<WrappedResponse<TripDto>> {

        val boat: BoatDto
        val crew: CrewDto
        try {
            boat = tripService.fetchBoat(dto.boatId!!)
            crew = tripService.fetchCrew(dto.crewId!!)

            // Check that crew size is within boat limits
            if(crew.crewSize!! > boat.maxCrewSize!!
                || crew.crewSize!! < boat.minCrewSize!!)
            {
                return RestResponseFactory.userFailure("Crew size has to be within boat limits")
            }
        } catch (e: Exception) {
            val res = WrappedResponse<Void>(500, message = "Something went wrong when deleting trip").validated()
            ResponseEntity.status(500).body(res)
        }

        val dtoWithId: TripDto

        try {
            val tripEntity = tripService.insertTrip(transform(dto))
            dtoWithId = transform(tripEntity)
        } catch(e: Exception) {
            return RestResponseFactory.userFailure("Could not insert in database, please check values")
        }

        return RestResponseFactory.payload(200, dtoWithId)
    }

    @DeleteMapping(path = ["/{tripId}"])
    fun deleteTrip(@PathVariable("tripId") tripId: Long): ResponseEntity<WrappedResponse<Void>> {
        return try {
            if(tripService.deleteTrip(tripId)) {
                RestResponseFactory.noPayload(200)
            } else {
                RestResponseFactory.notFound("No trip with given id")
            }
        } catch (e: Exception) {
            val res = WrappedResponse<Void>(500, message = "Something went wrong when deleting trip").validated()
            ResponseEntity.status(500).body(res)
        }
    }

    @PutMapping
    fun putTrip(@RequestBody dto: TripDto): ResponseEntity<WrappedResponse<TripDto>> {
        return try {
            val trip = tripService.putTrip(transform(dto))
            RestResponseFactory.payload(200, transform(trip))
        } catch (e: Exception) {
            val res = WrappedResponse<TripDto>(500, message = "Could not update trip: ${e.message}").validated()
            ResponseEntity.status(500).body(res)
        }
    }

}
