package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.model.CarWashWorker
import kz.innlab.carservice.general.repository.CarWashWorkerRepository
import kz.innlab.carservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarWashWorkerServiceImpl: CarWashWorkerService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarWashWorkerRepository

    @Autowired
    lateinit var washingCenterRepository: WashingCenterRepository
    override fun createCarWashWorker(carWashWorker: CarWashWorker): Status {
        val status = Status()

        washingCenterRepository.findByIdAndDeletedAtIsNull(carWashWorker.washingCenterId!!).ifPresent {
            carWashWorker.washingCenter = it
        }
        if (carWashWorker.washingCenter == null) {
            status.status = 0
            status.message = String.format("Washing Center: %s doesn't exist", carWashWorker.washingCenterId)
            status.value = carWashWorker.washingCenterId
            return status
        }
        repository.save(carWashWorker)
        status.status = 1
        status.message = String.format("Car Wash Worker: %s has been created", carWashWorker.fio)
        status.value = carWashWorker.id
        log.info(String.format("Car Wash Worker: %s has been created", carWashWorker.fio))
        return status
    }

    override fun editCarWashWorker(carWashWorker: CarWashWorker, carWashWorkerId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carWashWorkerId) ).ifPresentOrElse({
            it.avatar = carWashWorker.avatar
            it.firstName = carWashWorker.firstName
            it.lastName = carWashWorker.lastName
            it.phone = carWashWorker.phone

            repository.save(it)
            status.status = 1
            status.message = String.format("Car Wash Worker %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Car Wash Worker does not exist")
        })
        return status
    }

    override fun deleteCarWashWorker(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Wash Worker %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { carWashWorker ->
                carWashWorker.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(carWashWorker)

                status.status = 1
                status.message = String.format("Car Wash Worker %s has been deleted", id)
                log.info(String.format("Car Wash Worker %s has been deleted", id))
            }
        return status
    }

    override fun getCarWashWorkerList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashWorker> {
        return repository.findAllByWashingCenterId(washingCenterId)
    }

    override fun getCarWashWorkerById(id: UUID): Optional<CarWashWorker> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

}
