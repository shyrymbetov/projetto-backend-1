package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.CarWashPrice
import kz.innlab.carservice.general.model.Cars
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashPriceRepository: JpaRepository<CarWashPrice, UUID>, JpaSpecificationExecutor<CarWashPrice> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashPrice>
    fun findByWashingCenterIdAndDeletedAtIsNull(washingCenterId: UUID): List<CarWashPrice>
    fun findAllByIdInAndDeletedAtIsNull(carWashPriceIds: List<UUID>): List<CarWashPrice>
    fun findAllByDeletedAtIsNull(): List<CarWashPrice>
}
