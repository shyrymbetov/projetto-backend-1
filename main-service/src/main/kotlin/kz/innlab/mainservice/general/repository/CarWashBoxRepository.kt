package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarWashBox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashBoxRepository: JpaRepository<CarWashBox, UUID>, JpaSpecificationExecutor<CarWashBox> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashBox>
    fun findAllByWashingCenterId(id: UUID): List<CarWashBox>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<CarWashBox>
    fun findAllByDeletedAtIsNull(): List<CarWashBox>
}
