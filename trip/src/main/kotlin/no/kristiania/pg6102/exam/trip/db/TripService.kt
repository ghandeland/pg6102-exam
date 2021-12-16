package no.kristiania.pg6102.exam.trip.db

import no.kristiania.pg6102.exam.shared.dto.PortDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.rest.dto.WrappedResponse
import java.io.IOException
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
        circuitBreaker = circuitBreakerFactory.create("circuitBreakerToPort")
    }

    // TODO remove exceptions and replace with logging
    protected fun fetchData(): List<PortDto> {

        val uri = UriComponentsBuilder
            .fromUriString("http://${portAddress.trim()}/api/port")
            .build().toUri()

        val response = circuitBreaker.run(
            {
                client.exchange(uri, HttpMethod.GET, null, object : ParameterizedTypeReference<WrappedResponse<List<PortDto>>>() {})
            },
            { e ->
                throw IOException("Failed to fetch data: ${e.message}")
                null
            }
        ) ?: throw IOException("No response from server")

        if(response.statusCodeValue != 200) {
            throw IOException("Failed to fetch data: ${response.body.message} (Status ${response.statusCodeValue})")
        }

        try {
            return response.body.data!!
        } catch(e: Exception) {
            throw IOException("Failed to parse data: ${e.message}")
        }
    }

    fun fetchPortTemp(): List<PortDto> {
        return fetchData()
    }

    fun findByTripIdEager(tripId: Long): Trip? {
        return tripRepository.findById(tripId).orElse(null)
    }

    fun getAllTrips(): List<Trip> {
        return tripRepository.findAll()
    }




}