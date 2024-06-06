package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarFixBox
import java.util.*

interface CarFixBoxService {
    fun createCarFixBox(carFixBox: CarFixBox): Status
    fun editCarFixBox(carFixBox: CarFixBox, carFixBoxId: String): Status
    fun deleteCarFixBox(id: UUID): Status
    fun getCarFixBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFixBox>
    fun getCarFixBoxById(id: UUID): Optional<CarFixBox>
}
