package no.kristiania.pg6102.exam.port.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class Port {

    @get:Id @get:GeneratedValue
    var portId: Long? = null

    @get:NotBlank
    var name: String? = null

    @get:NotBlank
    var city: String? = null

    @get:NotBlank
    var country: String? = null
}