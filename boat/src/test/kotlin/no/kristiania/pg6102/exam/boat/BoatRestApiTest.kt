package no.kristiania.pg6102.exam.boat

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.kristiania.pg6102.exam.boat.db.BoatRepository
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles("BoatRestApiTest", "test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoatRestApiTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var boatRepository: BoatRepository

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun getBoatsTest() {

        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/boat")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(12))
    }

    @Test
    fun getBoatById() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/boat/1")
            .then()
            .statusCode(200)
            .body("data.name", equalTo("Fishing boat"))
    }

    @Test
    fun getBoatsWithinRange() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/boat/crewSize/3")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(2))
    }


}