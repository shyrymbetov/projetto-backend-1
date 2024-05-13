package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Cars
import java.util.*

interface CarService {
    fun createCar(cars: Cars, userId: String): Status
    fun editCar(cars: Cars, carId: String): Status
    fun deleteCar(id: UUID, userId: String): Status
    fun getCarsListMy(params: MutableMap<String, String>, owner: String): List<Cars>
    fun getCarById(id: UUID): Optional<Cars>
}
