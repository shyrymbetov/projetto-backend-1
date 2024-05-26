package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarFixBox
import java.util.*

interface CarFixBoxService {
    fun createCarFixBox(carFixBox: CarFixBox): Status
    fun editCarFixBox(carFixBox: CarFixBox, carFixBoxId: String): Status
    fun deleteCarFixBox(id: UUID): Status
    fun getCarFixBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFixBox>
    fun getCarFixBoxById(id: UUID): Optional<CarFixBox>
}
