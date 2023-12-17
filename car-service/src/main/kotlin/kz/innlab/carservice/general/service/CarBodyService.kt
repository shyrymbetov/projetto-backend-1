package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Cars
import java.util.*

interface CarBodyService {
    fun createCarBody(carBody: CarBody): Status
    fun editCarBody(carBody: CarBody, carBodyId: String): Status
    fun deleteCarBody(id: UUID): Status
    fun getCarBodyList(params: MutableMap<String, String>): List<CarBody>
    fun getCarBodyById(id: UUID): Optional<CarBody>
}
