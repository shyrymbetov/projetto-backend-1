package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarWashPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashPriceRepository: JpaRepository<CarWashPrice, UUID>, JpaSpecificationExecutor<CarWashPrice> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashPrice>
    fun findByWashingCenterIdAndCarBodyId(washingCenterId: UUID, carBodyId: UUID): Optional<CarWashPrice>
    fun findByWashingCenterIdAndDeletedAtIsNull(washingCenterId: UUID): List<CarWashPrice>
    fun findAllByIdInAndDeletedAtIsNull(carWashPriceIds: List<UUID>): List<CarWashPrice>
    fun findAllByDeletedAtIsNull(): List<CarWashPrice>
}
