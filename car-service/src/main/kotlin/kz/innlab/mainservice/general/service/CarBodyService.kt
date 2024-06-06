package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarBody
import java.util.*

interface CarBodyService {
    fun createCarBody(carBody: CarBody): Status
    fun editCarBody(carBody: CarBody, carBodyId: String): Status
    fun deleteCarBody(id: UUID): Status
    fun getCarBodyList(params: MutableMap<String, String>): List<CarBody>
    fun getCarBodyById(id: UUID): Optional<CarBody>
}
