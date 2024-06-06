package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.UserWashingCenterReview
import kz.innlab.mainservice.general.model.WashingCenter
import java.util.*

interface WashingCenterService {
    fun createWashingCenter(washingCenter: WashingCenter, userId: String): Status
    fun editWashingCenter(washingCenter: WashingCenter, washingCenterId: String): Status
    fun deleteWashingCenter(id: UUID, userId: String): Status
    fun getWashingCentersListMy(params: MutableMap<String, String>, employee: String): List<WashingCenter>
    fun getWashingCentersList(params: MutableMap<String, String>): List<WashingCenter>
    fun getWashingCenterById(id: UUID): Optional<WashingCenter>
    fun getListWashingCentersByIds(ids: List<UUID>): List<WashingCenter>
    fun getMyFavoriteWashingCenter(userId: String): List<WashingCenter>
    fun addFavoriteWashingCenter(id: UUID, userId: String): Status
    fun unFavoriteWashingCenter(userId: UUID, washingCenterId: UUID): Status
    fun addReviewWashingCenter(userId: UUID, washingCenterId: UUID, review: UserWashingCenterReview): Status
    fun deleteReviewWashingCenter(id: UUID): Status
    fun getReviewsByWashingCenter(id: UUID): List<UserWashingCenterReview>
    fun avatar(id: UUID, headingIds: List<UUID>): Status
}
