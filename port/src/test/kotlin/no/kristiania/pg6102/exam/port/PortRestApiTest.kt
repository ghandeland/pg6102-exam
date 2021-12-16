package no.kristiania.pg6102.exam.port

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.kristiania.pg6102.exam.port.db.PortRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles("PortRestApiTest")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PortRestApiTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var portRepository: PortRepository

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun getPortsTest() {

        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/port")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(8))
    }

    @Test
    fun getPortById() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/port/1")
            .then()
            .statusCode(200)
            .body("data.name", equalTo("Port of Helsinki"))
    }


}