package no.kristiania.pg6102.exam.trip

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.kristiania.pg6102.exam.shared.dto.BoatDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import no.kristiania.pg6102.exam.trip.TripDtoConverter.transform
import no.kristiania.pg6102.exam.trip.db.TripService
import no.kristiania.pg6102.exam.trip.dto.TripDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import no.kristiania.pg6102.exam.rest.dto.RestResponseFactory
import no.kristiania.pg6102.exam.rest.dto.WrappedResponse

@Api(value = "/api/trip", description = "Manages trips and data related to trips")
@RestController
@RequestMapping(path = ["/api/trip"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class TripRestApi(
    private val tripService: TripService
) {


    /*
    * Keyset pagination would not be useful here. A conceivable use case
    * for this endpoint, would be to return all data, so that the users
    * would be able to search for certain crews using functionality on
    * the frontend. This would not be possible with keyset pagination.
    */
    @ApiOperation("Retrieve all saved trips")
    @GetMapping
    fun getTrips(): ResponseEntity<WrappedResponse<List<TripDto>>> {
        return RestResponseFactory.payload(
            200,
            tripService.getAllTrips().map { transform(it) }
        )
    }

    @ApiOperation("Retrieve a trip entity based on trip ID")
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

    @ApiOperation("Creates a new trip. Checks if boat capacity matches crew size and that port IDs are different")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun postTrip(
        @ApiParam("Trip DTO object, that has to contain all properties except for ID")
        @RequestBody
        dto: TripDto
    ): ResponseEntity<WrappedResponse<TripDto>> {

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

    @ApiOperation("Delete an existing trip based on trip ID")
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

    @ApiOperation("Updates an existing trip, or creates a new one if it does not exist by ID")
    @PutMapping
    fun putTrip(
        @ApiParam("Trip DTO object, that has to contain all properties including ID")
        @RequestBody
        dto: TripDto
    ): ResponseEntity<WrappedResponse<TripDto>> {
        return try {
            val trip = tripService.putTrip(transform(dto))
            RestResponseFactory.payload(200, transform(trip))
        } catch (e: Exception) {
            val res = WrappedResponse<TripDto>(500, message = "Could not update trip: ${e.message}").validated()
            ResponseEntity.status(500).body(res)
        }
    }

}
