package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.CarWashTime
import kz.innlab.carservice.general.model.Cars
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashTimeRepository: JpaRepository<CarWashTime, UUID>, JpaSpecificationExecutor<CarWashTime> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashTime>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<CarWashTime>
    fun findAllByDeletedAtIsNull(): List<CarWashTime>
}
