package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarFixBox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarFixBoxRepository: JpaRepository<CarFixBox, UUID>, JpaSpecificationExecutor<CarFixBox> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarFixBox>
    fun findAllByWashingCenterId(id: UUID): List<CarFixBox>
    fun findAllByIdInAndDeletedAtIsNull(carFixBoxIds: List<UUID>): List<CarFixBox>
    fun findAllByDeletedAtIsNull(): List<CarFixBox>
}
