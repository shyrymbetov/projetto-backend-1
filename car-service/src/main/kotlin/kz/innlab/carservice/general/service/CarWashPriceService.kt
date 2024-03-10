package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.CarWashPrice
import kz.innlab.carservice.general.model.Cars
import java.util.*

interface CarWashPriceService {
    fun createCarWashPrice(carWashPrice: CarWashPrice): Status
    fun editCarWashPrice(carWashPrice: CarWashPrice, carWashPriceId: String): Status
    fun deleteCarWashPrice(id: UUID): Status
    fun getCarWashPriceListByCarWashingCenter(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashPrice>
    fun getCarWashPriceById(id: UUID): Optional<CarWashPrice>
}
