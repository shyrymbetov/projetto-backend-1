package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarWashPrice
import java.util.*

interface CarWashPriceService {
    fun createCarWashPrice(carWashPrice: CarWashPrice): Status
    fun editCarWashPrice(carWashPrice: CarWashPrice, carWashPriceId: String): Status
    fun deleteCarWashPrice(id: UUID): Status
    fun getCarWashPriceListByCarWashingCenter(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashPrice>
    fun getCarWashPriceById(id: UUID): Optional<CarWashPrice>
    fun getCarWashPriceByCarBodyAndWashingCenter(washingCenterId: UUID, carBodyId: UUID): Optional<CarWashPrice>
}
