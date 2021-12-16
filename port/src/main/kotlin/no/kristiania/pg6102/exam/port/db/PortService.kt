package no.kristiania.pg6102.exam.port.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface PortRepository : JpaRepository<Port, Long>

@Service
@Transactional
class PortService(
    private val portRepository: PortRepository,
    private val em: EntityManager
) {

    fun findByPortIdEager(portId: Long): Port? {
        return portRepository.findById(portId).orElse(null)
    }

    fun getAllPorts(): List<Port> {
        return portRepository.findAll()
    }

}