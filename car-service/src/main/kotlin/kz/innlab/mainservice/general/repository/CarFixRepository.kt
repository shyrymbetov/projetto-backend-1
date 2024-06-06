package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarFix
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarFixRepository: JpaRepository<CarFix, UUID>, JpaSpecificationExecutor<CarFix> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarFix>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<CarFix>
    fun findAllByDeletedAtIsNull(): List<CarFix>
    fun findByWashingCenterIdAndDeletedAtIsNull(washingCenterId: UUID): List<CarFix>
}
