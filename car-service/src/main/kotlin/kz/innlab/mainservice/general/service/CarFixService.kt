package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarFix
import java.util.*

interface CarFixService {
    fun createCarFix(carFix: CarFix): Status
    fun editCarFix(carFix: CarFix, carFixId: String): Status
    fun deleteCarFix(id: UUID): Status
    fun getCarFixList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFix>
    fun getCarFixById(id: UUID): Optional<CarFix>
}
