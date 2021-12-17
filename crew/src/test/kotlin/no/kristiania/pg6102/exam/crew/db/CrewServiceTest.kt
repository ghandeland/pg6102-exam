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

        assertEquals(crews.size, 18)
    }

    @Test
    fun testGetCrewsPagination(){

        val n = 5
        val page = crewService.getNextPage(n)
        assertEquals(n, page.size)

        for(i in 0 until n-1) {
            assertTrue(page[i].crewSize!! >= page[i+1].crewSize!!)
        }
    }

    @Test
    fun testGetCrewById() {
        val serviceCrew = crewService.findCrewByIdEager("c000")

        assertNotNull(serviceCrew)
        assertEquals(serviceCrew!!.crewName, "Crew Alpha")
    }
}