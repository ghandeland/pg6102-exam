package no.kristiania.pg6102.exam.crew

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import no.kristiania.pg6102.exam.crew.db.CrewRepository
import no.kristiania.pg6102.exam.rest.dto.PageDto
import no.kristiania.pg6102.exam.shared.dto.CrewDto
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

@ActiveProfiles("CrewRestApiTest", "test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrewRestApiTest {

    @LocalServerPort
    protected var port = 0


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
            .body("data.list.size()", equalTo(18))
    }

    @Test
    fun getCrewByIdTest() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .get("/api/crew/c001")
            .then()
            .statusCode(200)
            .body("data.crewName", equalTo("Crew Bravo"))
    }

    @Test
    fun get2PagesTest() {

        val page = RestAssured.given().accept(ContentType.JSON)
            .get("/api/crew/pagination")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(10))
            .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String, Object>>>(){})


        RestAssured.given().accept(ContentType.JSON)
            .get(page.next)
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(8))
    }
}