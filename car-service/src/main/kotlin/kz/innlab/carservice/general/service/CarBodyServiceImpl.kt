package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Cars
import kz.innlab.carservice.general.repository.CarBodyRepository
import kz.innlab.carservice.general.repository.CarRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarBodyServiceImpl : CarBodyService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarBodyRepository

//    @Autowired
//    lateinit var carBodyRepository: CarBodyRepository
//
//    //    @Autowired
//    //    lateinit var fileServiceClient: FileServiceClient

    override fun createCarBody(carBody: CarBody): Status {
        val status = Status()

        repository.save(carBody)

        status.status = 1
        status.message = String.format("Car Body: %s has been created", carBody.type)
        status.value = carBody.id
        log.info(String.format("Car Body: %s has been created", carBody.type))
        return status
    }

    override fun editCarBody(carBody: CarBody, carBodyId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carBodyId) ).ifPresentOrElse({
            it.type = carBody.type
            repository.save(it)
            status.status = 1
            status.message = String.format("Car Body %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Car Body does not exist")
        })
        return status

    }

    override fun deleteCarBody(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Body %s does not exist", id)
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

    override fun getCarBodyList(params: MutableMap<String, String>): List<CarBody> {
        return repository.findAllByDeletedAtIsNull()
    }

    override fun getCarBodyById(id: UUID): Optional<CarBody> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }




}
