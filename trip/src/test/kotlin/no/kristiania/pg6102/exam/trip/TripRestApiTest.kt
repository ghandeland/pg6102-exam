package no.kristiania.pg6102.exam.trip

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.kristiania.pg6102.exam.trip.db.TripService
import no.kristiania.pg6102.exam.trip.dto.TripDto
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import no.kristiania.pg6102.exam.rest.dto.WrappedResponse
import javax.annotation.PostConstruct


@ActiveProfiles("TripRestApiTest","test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(TripRestApiTest.Companion.Initializer::class)])
class TripRestApiTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var tripService: TripService

    //@Autowired
    //private lateinit var userRepository: UserRepository

    companion object {

        private lateinit var wiremockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServer = WireMockServer(
                WireMockConfiguration
                    .wireMockConfig()
                    .dynamicPort()
                    .notifier(ConsoleNotifier(true))
            )
            wiremockServer.start()

            val boatRes = WrappedResponse(code = 200, data = DtoMocks.getBoat()).validated()
            val crewRes = WrappedResponse(code = 200, data = DtoMocks.getCrew()).validated()

            wiremockServer.stubFor(
                get(WireMock.urlMatching("/api/boat_*"))
                    .willReturn(
                        ok(ObjectMapper().writeValueAsString(boatRes))
                            .withHeader("Content-Type", "application/json; charset=utf-8")
                    )
            )

            wiremockServer.stubFor(
                get(WireMock.urlMatching("/api/crew_*"))
                    .willReturn(
                        ok(ObjectMapper().writeValueAsString(crewRes))
                            .withHeader("Content-Type", "application/json; charset=utf-8")
                    )
            )
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "boatServiceAddress: localhost:${wiremockServer.port()}",
                    "crewServiceAddress: localhost:${wiremockServer.port()}")
                    .applyTo(configurableApplicationContext.environment)
            }
        }
    }


    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/trip"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testGetTrips(){

        RestAssured.given()
            .accept(ContentType.JSON)
            .get()
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(6))
    }

    @Test
    fun testWrongCrewSizeForBoat(){

        RestAssured.given()
            .accept(ContentType.JSON)
            .get()
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(6))
    }

    @Test
    fun testPostTrip(){

        val dto = TripDto(null, 2, 1, 3, "c007")

        val sizeBefore = RestAssured.given()
            .accept(ContentType.JSON)
            .get()
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getInt("data.list.size()")

        RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(200)
            .body("data.tripId", not(equalTo(null)))

        val sizeAfter = RestAssured.given()
            .accept(ContentType.JSON)
            .get()
            .then()
            .statusCode(200)
            .body("data.tripId", not(equalTo(null)))
            .extract().body().jsonPath().getInt("data.list.size()")

        assertEquals(sizeAfter, (sizeBefore + 1))
    }

    @Test
    fun testGetTripById(){

        val dto = TripDto(null, 2, 1, 7, "c002")

        val dtoId = RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("data.tripId")

        // Get by earlier posted ID and check values against dto object
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/$dtoId")
            .then()
            .statusCode(200)
            .body("data.crewId", equalTo(dto.crewId))
            .body("data.boatId", equalTo(dto.boatId!!.toInt()))
    }

    // Put has to provide full objects, this asserts that excluding a property won't work
    @Test
    fun testPutTripWithoutId() {

        val dto = TripDto(null, 2, 1, 3, "c007")

        // Post new entity
        RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("data.tripId")

        // Assert that value has been changed on response
        RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .put()
            .then()
            .statusCode(500)
            .body("message", endsWith("Trip object has missing ID"))
    }

    @Test
    fun testPutTrip() {

        val dto = TripDto(null, 2, 1, 3, "c007")

        // Post new entity
        val dtoId = RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("data.tripId")

        // Change value and add ID
        dto.tripId = dtoId
        dto.destinationPortId = 5

        // Assert that value has been changed on response
        RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .put()
            .then()
            .statusCode(200)
            .body("data.destinationPortId", equalTo(5))
    }

    @Test
    fun testDeleteTrip() {

        val dto = TripDto(null, 2, 1, 3, "c007")

        // Post new Trip and extract ID
        val dtoId = RestAssured.given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getLong("data.tripId")

        // Delete by ID
        RestAssured.given()
            .contentType(ContentType.JSON)
            .delete("/$dtoId")
            .then()
            .statusCode(200)

        // Try to get by ID and assert 404
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/$dtoId")
            .then()
            .statusCode(404)
    }

}