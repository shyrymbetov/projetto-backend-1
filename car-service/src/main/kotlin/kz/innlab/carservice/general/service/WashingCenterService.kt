package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.WashingCenter
import java.util.*

interface WashingCenterService {
    fun createWashingCenter(washingCenter: WashingCenter, userId: String): Status
    fun editWashingCenter(washingCenter: WashingCenter, washingCenterId: String): Status
    fun deleteWashingCenter(id: UUID, userId: String): Status
    fun getWashingCentersListMy(params: MutableMap<String, String>, employee: String): List<WashingCenter>
    fun getWashingCenterById(id: UUID): Optional<WashingCenter>
    fun addFavoriteWashingCeter(id: UUID, userId: String): Status
}
