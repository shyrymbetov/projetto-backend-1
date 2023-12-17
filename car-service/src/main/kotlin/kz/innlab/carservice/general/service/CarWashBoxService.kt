package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.Cars
import java.util.*

interface CarWashBoxService {
    fun createCarWashBox(carWashBox: CarWashBox): Status
    fun editCarWashBox(carWashBox: CarWashBox, carWashBoxId: String): Status
    fun deleteCarWashBox(id: UUID): Status
    fun getCarWashBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashBox>
    fun getCarWashBoxById(id: UUID): Optional<CarWashBox>
}
