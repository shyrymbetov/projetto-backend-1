package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.repository.CarWashBoxRepository
import kz.innlab.carservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarWashBoxServiceImpl: CarWashBoxService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarWashBoxRepository

    @Autowired
    lateinit var washingCenterRepository: WashingCenterRepository

    override fun createCarWashBox(carWashBox: CarWashBox): Status {
        val status = Status()

        washingCenterRepository.findByIdAndDeletedAtIsNull(carWashBox.washingCenterId!!).ifPresent {
            carWashBox.washingCenter = it
        }
        if (carWashBox.washingCenter == null) {
            status.status = 0
            status.message = String.format("Washing Center: %s doesn't exist", carWashBox.washingCenterId)
            status.value = carWashBox.washingCenterId
            return status
        }
        repository.save(carWashBox)
        status.status = 1
        status.message = String.format("Car Wash Box: %s has been created", carWashBox.name)
        status.value = carWashBox.id
        log.info(String.format("Car Wash Box: %s has been created", carWashBox.name))
        return status
    }

    override fun editCarWashBox(carWashBox: CarWashBox, carWashBoxId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carWashBoxId) ).ifPresentOrElse({

            it.name = carWashBox.name
            // TODO update carWashTime
            repository.save(it)
            status.status = 1
            status.message = String.format("Car Wash Box %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Car Wash Box does not exist")
        })
        return status
    }

    override fun deleteCarWashBox(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Wash Box %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { carWashBox ->
                carWashBox.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(carWashBox)

                status.status = 1
                status.message = String.format("Car %s has been deleted", id)
                log.info(String.format("Car %s has been deleted", id))
            }
        return status
    }

    override fun getCarWashBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarWashBox> {
        return repository.findAllByWashingCenterId(washingCenterId)
    }

    override fun getCarWashBoxById(id: UUID): Optional<CarWashBox> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

}
