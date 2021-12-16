package no.kristiania.pg6102.exam.crew.db

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class CrewServiceTest {

    @Autowired
    private lateinit var crewService: CrewService

    @Test
    fun testGetAllCrews() {
        val crews = crewService.getAllCrews()

        assertEquals(crews.size, 10)
    }

    @Test
    fun testGetCrewById() {
        val serviceCrew = crewService.findCrewByIdEager("c000")

        assertNotNull(serviceCrew)
        assertEquals(serviceCrew!!.crewName, "Crew Alpha")
    }
}