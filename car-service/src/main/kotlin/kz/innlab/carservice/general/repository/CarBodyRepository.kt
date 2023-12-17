package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Cars
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarBodyRepository: JpaRepository<CarBody, UUID>, JpaSpecificationExecutor<CarBody> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarBody>
    fun findByTypeAndDeletedAtIsNull(type: String): Optional<CarBody>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<CarBody>
    fun findAllByDeletedAtIsNull(): List<CarBody>
}
