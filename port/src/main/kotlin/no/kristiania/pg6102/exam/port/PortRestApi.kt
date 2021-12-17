package no.kristiania.pg6102.exam.port

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.kristiania.pg6102.exam.port.db.PortService
import no.kristiania.pg6102.exam.shared.dto.PortDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import no.kristiania.pg6102.exam.rest.dto.RestResponseFactory
import no.kristiania.pg6102.exam.rest.dto.WrappedResponse

@Api(value = "/api/port", description = "Servers data related to ports, including names and location")
@RestController
@RequestMapping(path = ["/api/port"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
class PortRestApi(
    private val portService: PortService
) {

    /*
    * Because of the nature of port data, it is bounded and will
    * not reach a quantity where pagination will be necessary.
    */
    @ApiOperation("Retrieves all ports saved in database")
    @GetMapping
    fun getPorts(): ResponseEntity<WrappedResponse<List<PortDto>>> {
        return RestResponseFactory.payload(
            200,
            portService.getAllPorts().map { PortDtoConverter.transform(it) }
        )
    }

    @GetMapping(path = ["/{portId}"])
    @ApiOperation("Retrieves a port entity based on ID")
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