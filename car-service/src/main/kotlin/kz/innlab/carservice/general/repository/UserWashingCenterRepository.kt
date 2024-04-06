package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface UserWashingCenterRepository: JpaRepository<UserWashingCenter, UUID>, JpaSpecificationExecutor<UserWashingCenter> {
//    fun findById(id: UUID): Optional<UserWashingCenter>
}
