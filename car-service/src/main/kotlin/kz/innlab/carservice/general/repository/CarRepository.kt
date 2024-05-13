package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.Cars
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CarRepository: JpaRepository<Cars, UUID>, JpaSpecificationExecutor<Cars> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Cars>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<Cars>
    fun findAllByDeletedAtIsNull(): List<Cars>
    fun findByVrpAndDeletedAtIsNull(vrp: String): Optional<Cars>
    fun findByOwnerAndDeletedAtIsNull(id: UUID): List<Cars>
}
