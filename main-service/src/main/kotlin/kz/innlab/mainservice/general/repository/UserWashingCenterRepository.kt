package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface UserWashingCenterRepository: JpaRepository<UserWashingCenter, UUID>, JpaSpecificationExecutor<UserWashingCenter> {
    fun findAllByUserId(id: UUID): List<UserWashingCenter>
    fun findByUserIdAndWashingCenterId(userId: UUID, washingCenterId: UUID): Optional<UserWashingCenter>
}
