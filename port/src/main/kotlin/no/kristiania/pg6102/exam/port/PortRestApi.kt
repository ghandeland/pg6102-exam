package no.kristiania.pg6102.exam.port

import no.kristiania.pg6102.exam.port.db.PortService
import no.kristiania.pg6102.exam.shared.dto.PortDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/api/port"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class PortRestApi(
    private val portService: PortService
) {

    @GetMapping
    fun getPorts(): ResponseEntity<WrappedResponse<List<PortDto>>> {
        return RestResponseFactory.payload(
            200,
            portService.getAllPorts().map { PortDtoConverter.transform(it) }
        )
    }

    @GetMapping(path = ["/{portId}"])
    fun getPortById(
        @PathVariable("portId") portId: Long
    ): ResponseEntity<WrappedResponse<PortDto>> {
        val port = portService.findByPortIdEager(portId)
        if(port == null) {
            return ResponseEntity.notFound().build()
        }

        return RestResponseFactory.payload(200, PortDtoConverter.transform(port))
    }


}