package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface WashingCenterRepository: JpaRepository<WashingCenter, UUID>, JpaSpecificationExecutor<WashingCenter> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<WashingCenter>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<WashingCenter>
    fun findAllByDeletedAtIsNull(): List<WashingCenter>
    fun findAllByEmployeeAndDeletedAtIsNull(id: UUID): List<WashingCenter>
}
