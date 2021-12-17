package no.kristiania.pg6102.exam.trip.db

import no.kristiania.pg6102.exam.shared.dto.BoatDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
import no.kristiania.pg6102.exam.shared.dto.PortDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.rest.dto.WrappedResponse
import java.io.IOException
import java.net.URI
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Repository
interface TripRepository : JpaRepository<Trip, Long>

@Service
@Transactional
class TripService(
    private val circuitBreakerFactory: Resilience4JCircuitBreakerFactory,
    private val tripRepository: TripRepository,
    private val em: EntityManager
) {

    @Value("\${portServiceAddress}")
    private lateinit var portAddress: String

    @Value("\${crewServiceAddress}")
    private lateinit var crewAddress: String

    @Value("\${boatServiceAddress}")
    private lateinit var boatAddress: String

    private val client: RestTemplate = RestTemplate()

    private lateinit var circuitBreaker: CircuitBreaker

    private val lock = Any()

    @PostConstruct
    fun init() {
        circuitBreaker = circuitBreakerFactory.create("tripServiceCircuitBreaker")
    }

    protected fun fetchBoat(uri: URI): BoatDto {

        val response = circuitBreaker.run(
            {
                client.exchange(uri, HttpMethod.GET, null, object : ParameterizedTypeReference<WrappedResponse<BoatDto>>() {})
            },
            {
                throw IOException("Failed to fetch boat from service")
                null
            }
        ) ?: throw IOException("No response from boat service")

        validateResponse(response)
        return response.body.data!!
    }

    protected fun fetchCrew(uri: URI): CrewDto {

        val response = circuitBreaker.run(
            {
                client.exchange(uri, HttpMethod.GET, null, object : ParameterizedTypeReference<WrappedResponse<CrewDto>>() {})
            },
            {
                throw IOException("Failed to fetch crew from service")
                null
            }
        ) ?: throw IOException("No response from crew service")

        validateResponse(response)
        return response.body.data!!
    }

    private fun buildUri(address: String, endpoint: String, pathValue: String): URI {
        val uri = UriComponentsBuilder
            .fromUriString("http://${address.trim()}/$endpoint$pathValue")
            .build().toUri()
        return uri
    }

    private fun <T> validateResponse(response: ResponseEntity<WrappedResponse<T>>) {
        if (response.statusCodeValue != 200) {
            throw IOException("Bad response from service: ${response.body.message} (${response.statusCodeValue})")
        }

        if(response.body.data == null) {
            throw IOException("Fetched data was not parsed correctly")
        }
    }

    fun fetchCrew(crewId: String): CrewDto {
        val uri = buildUri(crewAddress, "api/crew", "/$crewId")
        return fetchCrew(uri)
    }

    fun fetchBoat(boatId: Long): BoatDto {
        val uri = buildUri(boatAddress, "api/boat", "/$boatId")
        return fetchBoat(uri)
    }

    fun findByTripIdEager(tripId: Long): Trip? {
        return tripRepository.findById(tripId).orElse(null)
    }

    fun getAllTrips(): List<Trip> {
        return tripRepository.findAll()
    }

    fun insertTrip(trip: Trip): Trip {
        validateTrip(trip)
        return tripRepository.save(trip)
    }

    fun deleteTrip(tripId: Long): Boolean {
        val trip = findByTripIdEager(tripId)
        if(trip !== null) {
            tripRepository.delete(trip)
            return true
        }
        return false
    }

    fun putTrip(trip: Trip): Trip {
        validateTrip(trip, true)
        return tripRepository.save(trip)
    }

    fun validateTrip(trip: Trip, includeId: Boolean = false) {
        if(includeId) {
            if(trip.tripId == null) {
                throw IllegalStateException("Trip object has missing ID")
            }
        }

        if(trip.boatId == null
            || trip.crewId == null
            || trip.originPortId == null
            || trip.destinationPortId == null) {
            throw IllegalStateException("Trip object has missing data")
        }

        if(trip.originPortId == trip.destinationPortId) {
            throw IllegalStateException("Origin port and destination port have to contain different IDs")
        }
    }


}