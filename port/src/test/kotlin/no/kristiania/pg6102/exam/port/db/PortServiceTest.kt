package no.kristiania.pg6102.exam.port.db

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
internal class PortServiceTest {

    @Autowired
    private lateinit var portService: PortService

    @Test
    fun testGetAllPorts() {
        val ports = portService.getAllPorts()

        assertEquals(ports.size, 8)
    }

    @Test
    fun testGetPortById() {
        val servicePort = portService.findByPortIdEager(1)

        assertNotNull(servicePort)
    }
}