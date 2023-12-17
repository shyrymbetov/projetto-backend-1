package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Cars
import kz.innlab.carservice.general.repository.CarBodyRepository
import kz.innlab.carservice.general.repository.CarRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.rmi.UnexpectedException
import java.sql.Timestamp
import java.util.*

@Service
class CarServiceImpl : CarService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarRepository

    @Autowired
    lateinit var carBodyRepository: CarBodyRepository

    //    @Autowired
    //    lateinit var fileServiceClient: FileServiceClient

    override fun createCar(cars: Cars, userId: String): Status {
        val status = Status()

        cars.owner = UUID.fromString(userId)
        carBodyRepository.findByIdAndDeletedAtIsNull(cars.carBodyId!!).ifPresent {
                cars.carBody = it
        }
        if (cars.carBody == null) {
            status.status = 1
            status.message = String.format("Car Body: %s doesn't exist", cars.carBodyId)
            status.value = cars.id
            return status
        }
        repository.save(cars)
        status.status = 1
        status.message = String.format("Car: %s has been created", cars.model + " " + cars.mark)
        status.value = cars.id
        log.info(String.format("Car: %s has been created", cars.model + " " + cars.mark))
        return status
    }

    override fun editCar(cars: Cars, carId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carId) ).ifPresentOrElse({
            it.model = cars.model
            it.mark = cars.model
             carBodyRepository.findByIdAndDeletedAtIsNull(it.carBody!!.id!!).ifPresent { carBody ->
                 it.carBody = carBody
            }
            it.color = cars.color
            it.vrp = cars.vrp
            repository.save(it)
            status.status = 1
            status.message = String.format("Car %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Car does not exist")
        })
        return status
    }

    override fun deleteCar(id: UUID, userId: String): Status {
        val status = Status()
        status.message = String.format("Car %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { book ->
                book.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(book)

                status.status = 1
                status.message = String.format("Car %s has been deleted", id)
                log.info(String.format("Car %s has been deleted", id))
            }
        return status
    }

    override fun getCarsListMy(params: MutableMap<String, String>, owner: String): List<Cars> {
        val cars = repository.findByOwnerAndDeletedAtIsNull(UUID.fromString(owner))
        return cars
    }

    override fun getCarById(id: UUID): Optional<Cars> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }



}
