package no.kristiania.pg6102.exam.crew

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.kristiania.pg6102.exam.crew.db.CrewRepository
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles("CrewRestApiTest")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrewRestApiTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var crewRepository: CrewRepository

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun getCrewsTest() {

        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/crew")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(10))
    }

    @Test
    fun getCrewById() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/crew/c001")
            .then()
            .statusCode(200)
            .body("data.crewName", equalTo("Crew Bravo"))
    }
}