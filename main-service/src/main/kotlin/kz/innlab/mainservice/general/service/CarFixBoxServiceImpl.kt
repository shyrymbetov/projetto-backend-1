package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarFixBox
import kz.innlab.mainservice.general.repository.CarFixBoxRepository
import kz.innlab.mainservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarFixBoxServiceImpl: CarFixBoxService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarFixBoxRepository

    @Autowired
    lateinit var washingCenterRepository: WashingCenterRepository


    override fun createCarFixBox(carFixBox: CarFixBox): Status {
        val status = Status()

        washingCenterRepository.findByIdAndDeletedAtIsNull(carFixBox.washingCenterId!!).ifPresent {
            carFixBox.washingCenter = it
        }
        if (carFixBox.washingCenter == null) {
            status.status = 0
            status.message = String.format("Washing Center: %s doesn't exist", carFixBox.washingCenterId)
            status.value = carFixBox.washingCenterId
            return status
        }
        repository.save(carFixBox)
        status.status = 1
        status.message = String.format("Car Wash Box: %s has been created", carFixBox.name)
        status.value = carFixBox.id
        log.info(String.format("Car Wash Box: %s has been created", carFixBox.name))
        return status
    }

    override fun editCarFixBox(carFixBox: CarFixBox, carFixBoxId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carFixBoxId) ).ifPresentOrElse({

            it.name = carFixBox.name
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

    override fun deleteCarFixBox(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Wash Box %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { CarFixBox ->
                CarFixBox.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(CarFixBox)

                status.status = 1
                status.message = String.format("Car %s has been deleted", id)
                log.info(String.format("Car %s has been deleted", id))
            }
        return status
    }

    override fun getCarFixBoxList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFixBox> {
        return repository.findAllByWashingCenterId(washingCenterId)
    }

    override fun getCarFixBoxById(id: UUID): Optional<CarFixBox> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

}
