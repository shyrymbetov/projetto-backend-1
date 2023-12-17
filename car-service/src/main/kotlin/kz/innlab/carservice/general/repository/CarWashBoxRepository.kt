package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.Cars
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashBoxRepository: JpaRepository<CarWashBox, UUID>, JpaSpecificationExecutor<CarWashBox> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashBox>
    fun findAllByWashingCenterId(id: UUID): List<CarWashBox>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<CarWashBox>
    fun findAllByDeletedAtIsNull(): List<CarWashBox>
}
