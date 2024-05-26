package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.CarFix
import kz.innlab.carservice.general.model.Cars
import kz.innlab.carservice.general.repository.CarBodyRepository
import kz.innlab.carservice.general.repository.CarFixRepository
import kz.innlab.carservice.general.repository.CarRepository
import kz.innlab.carservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarFixServiceImpl : CarFixService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarFixRepository

    @Autowired
    lateinit var washingCenterRepository: WashingCenterRepository


//    //    @Autowired
//    //    lateinit var fileServiceClient: FileServiceClient


    override fun createCarFix(carFix: CarFix): Status {
        val status = Status()


        washingCenterRepository.findByIdAndDeletedAtIsNull(carFix.washingCenterId!!).ifPresent {
            carFix.washingCenter = it
        }
        if (carFix.washingCenter == null) {
            status.status = 0
            status.message = String.format("Washing Center: %s doesn't exist", carFix.washingCenterId)
            status.value = carFix.washingCenterId
            return status
        }

        status.status = 1
        status.message = String.format("Car Fix: %s has been created", carFix.ruName)
        status.value = carFix.ruName
        log.info(String.format("Car Fix: %s has been created", carFix.ruName))
        repository.save(carFix)
        return status
    }

    override fun editCarFix(carFix: CarFix, carFixId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carFixId) ).ifPresentOrElse({
            it.ruName = carFix.ruName
            it.cost = it.cost
            repository.save(it)
            status.status = 1
            status.message = String.format("Car Fix %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Car Fix does not exist")
        })
        return status

    }

    override fun deleteCarFix(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Fix %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { book ->
                book.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(book)

                status.status = 1
                status.message = String.format("Car Fix %s has been deleted", id)
                log.info(String.format("Car Fix %s has been deleted", id))
            }
        return status
    }

    override fun getCarFixList(params: MutableMap<String, String>, washingCenterId: UUID): List<CarFix> {
        return repository.findByWashingCenterIdAndDeletedAtIsNull(washingCenterId)
    }

    override fun getCarFixById(id: UUID): Optional<CarFix> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }


}
