package no.kristiania.pg6102.exam.boat.db

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
internal class BoatServiceTest {

    @Autowired
    private lateinit var boatService: BoatService

    @Test
    fun testGetAllBoats() {
        val boats = boatService.getAllBoats()

        assertEquals(boats.size, 12)
    }

    @Test
    fun testGetPortById() {
        val serviceBoat = boatService.findBoatByIdEager(1)

        assertNotNull(serviceBoat)
        assertEquals(serviceBoat!!.name, "Evergreen")
    }

    @Test
    fun testGetBoatsWithinRange() {
        val boats = boatService.getBoatsWithinRange(3)

        assertEquals(boats.size, 2)
        assertTrue(boats.any { it.name == "Fishing boat" })
    }

}