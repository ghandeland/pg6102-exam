package no.kristiania.pg6102.exam.crew.db

import no.kristiania.pg6102.exam.shared.dto.CrewDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Repository
interface CrewRepository : JpaRepository<Crew, String>

@Service
@Transactional
class CrewService(
    private val crewRepository: CrewRepository,
    private val em: EntityManager
) {

    /*
    * This code in this function has been adapted from the keyset
    * functionality introduced in lecture 4 in the course repository:
    * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-04/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsService.kt
    */

    fun getNextPage(size: Int, keysetId: String? = null, keysetCrewSize: Int? = null): List<Crew>{

        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value: $size")
        }

        if((keysetId==null && keysetCrewSize!=null) || (keysetId!=null && keysetCrewSize==null)){
            throw IllegalArgumentException("keysetId and keysetCrewSize should be both missing, or both present")
        }

        val query: TypedQuery<Crew>
        if (keysetId == null) {
            query = em.createQuery(
                "select c from Crew c order by c.crewSize DESC, c.crewId DESC"
                , Crew::class.java)
        } else {
            query = em.createQuery(
                "select c from Crew c where c.crewSize <?2 or (c.crewSize=?2 and c.crewId<?1) order by c.crewSize DESC, c.crewId DESC"
                , Crew::class.java)
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetCrewSize)
        }
        query.maxResults = size

        return query.resultList
    }


    fun findCrewByIdEager(crewId: String): Crew? {
        return crewRepository.findById(crewId).orElse(null)
    }

    fun getAllCrews(): List<Crew> {
        return crewRepository.findAll()
    }
}