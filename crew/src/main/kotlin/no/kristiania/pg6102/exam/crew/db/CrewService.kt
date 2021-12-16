package no.kristiania.pg6102.exam.crew.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface CrewRepository : JpaRepository<Crew, String>

@Service
@Transactional
class CrewService(
    private val crewRepository: CrewRepository,
    private val em: EntityManager
) {

    fun findCrewByIdEager(crewId: String): Crew? {
        return crewRepository.findById(crewId).orElse(null)
    }

    fun getAllCrews(): List<Crew> {
        return crewRepository.findAll()
    }
}