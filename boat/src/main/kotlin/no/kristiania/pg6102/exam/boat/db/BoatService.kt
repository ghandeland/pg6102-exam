package no.kristiania.pg6102.exam.boat.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Repository
interface BoatRepository : JpaRepository<Boat, Long>

@Service
@Transactional
class BoatService(
    private val boatRepository: BoatRepository,
    private val em: EntityManager
) {

    fun findBoatByIdEager(boatId: Long): Boat? {
        return boatRepository.findById(boatId).orElse(null)
    }

    fun getAllBoats(): List<Boat> {
        return boatRepository.findAll()
    }

    fun getBoatsWithinRange(crewSize: Int): List<Boat> {

        if(crewSize < 1) {
            throw IllegalArgumentException("Crew amount cannot be less than 1")
        }

        val query: TypedQuery<Boat> = em.createQuery(
            "select b from Boat b where b.minCrewSize <= ?1 and b.maxCrewSize>=?1",
            Boat::class.java
        ).setParameter(1, crewSize)

        return query.resultList
    }

}