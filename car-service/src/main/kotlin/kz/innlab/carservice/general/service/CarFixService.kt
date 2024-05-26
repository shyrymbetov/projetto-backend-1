package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarFix
import kz.innlab.carservice.general.model.Cars
import java.util.*

interface CarFixService {
    fun createCarFix(carFix: CarFix): Status
    fun editCarFix(carFix: CarFix, carFixId: String): Status
    fun deleteCarFix(id: UUID): Status
    fun getCarFixList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFix>
    fun getCarFixById(id: UUID): Optional<CarFix>
}
