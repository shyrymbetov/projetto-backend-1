package kz.innlab.mainservice.general.service


import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.Cars
import kz.innlab.mainservice.general.repository.CarBodyRepository
import kz.innlab.mainservice.general.repository.CarRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
            status.status = 0
            status.message = String.format("Car body: %s doesn't exist", cars.carBodyId)
            status.value = cars.id
            return status
        }

        repository.findByVrpAndDeletedAtIsNull(cars.vrp!!).ifPresent {
            status.status = 0
            status.message = String.format("VRP: %s already in use", cars.vrp)
            status.value = cars.id
        }
        repository.save(cars)
        status.status = 1
        status.message = String.format("Car: %s has been created", cars.model + " " + cars.mark)
        status.value = cars.id
        log.info(String.format("Car: %s has been created", cars.model + " " + cars.mark))
        return status
    }

    override fun editCar(cars: Cars, carId: String): Status {
        var status = Status()
        status.status = 1
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carId) ).ifPresentOrElse({
            it.model = cars.model
            it.mark = cars.mark
            carBodyRepository.findByIdAndDeletedAtIsNull(it.carBodyId!!).ifPresent { carBody ->
                it.carBody = carBody
            }

            if (it.carBody == null) {
                status.status = 0
                status.message = String.format("Car body: %s doesn't exist", cars.carBodyId)
                status.value = cars.id
            }

            it.color = cars.color
            repository.findByVrpAndDeletedAtIsNull(cars.vrp!!).ifPresent {
                if (it.vrp != cars.vrp) {
                    status.status = 0
                    status.message = String.format("VRP: %s already in use", cars.vrp)
                    status.value = cars.id
                }
            }
            it.vrp = cars.vrp
            if (status.status == 1) {
                repository.save(it)
                status.status = 1
                status.message = String.format("Car %s has been edited", it.id)
                status.value = it.id
            }

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