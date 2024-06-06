package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarBody
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarBodyRepository: JpaRepository<CarBody, UUID>, JpaSpecificationExecutor<CarBody> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarBody>
    fun findByTypeAndDeletedAtIsNull(type: String): Optional<CarBody>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<CarBody>
    fun findAllByDeletedAtIsNull(): List<CarBody>
}
