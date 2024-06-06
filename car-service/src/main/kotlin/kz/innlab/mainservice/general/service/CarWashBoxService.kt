package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarWashBox
import java.util.*

interface CarWashBoxService {
    fun createCarWashBox(carWashBox: CarWashBox): Status
    fun editCarWashBox(carWashBox: CarWashBox, carWashBoxId: String): Status
    fun deleteCarWashBox(id: UUID): Status
    fun getCarWashBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashBox>
    fun getCarWashBoxById(id: UUID): Optional<CarWashBox>
}
