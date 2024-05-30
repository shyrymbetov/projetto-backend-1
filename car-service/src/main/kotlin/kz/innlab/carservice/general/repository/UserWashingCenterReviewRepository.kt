package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface UserWashingCenterReviewRepository: JpaRepository<UserWashingCenterReview, UUID>, JpaSpecificationExecutor<UserWashingCenterReview> {
    fun findAllByWashingCenterId(id: UUID): List<UserWashingCenterReview>
    fun findByUserIdAndWashingCenterId(userId: UUID, washingCenterId: UUID): Optional<UserWashingCenterReview>
}
