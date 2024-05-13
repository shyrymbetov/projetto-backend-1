package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarWashWorker
import java.util.*

interface CarWashWorkerService {
    fun createCarWashWorker(carWashWorker: CarWashWorker): Status
    fun editCarWashWorker(carWashWorker: CarWashWorker, carWashWorkerId: String): Status
    fun deleteCarWashWorker(id: UUID): Status
    fun getCarWashWorkerList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashWorker>
    fun getCarWashWorkerById(id: UUID): Optional<CarWashWorker>
}
