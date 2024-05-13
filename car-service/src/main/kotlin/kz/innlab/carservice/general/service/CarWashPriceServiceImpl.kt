package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarWashPrice
import kz.innlab.carservice.general.repository.CarBodyRepository
import kz.innlab.carservice.general.repository.CarWashPriceRepository
import kz.innlab.carservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CarWashPriceServiceImpl: CarWashPriceService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: CarWashPriceRepository

    @Autowired
    lateinit var washingCenterRepository: WashingCenterRepository

    @Autowired
    lateinit var carBodyRepository: CarBodyRepository
    override fun createCarWashPrice(carWashPrice: CarWashPrice): Status {
        val status = Status()

        washingCenterRepository.findByIdAndDeletedAtIsNull(carWashPrice.washingCenterId!!).ifPresent {
            carWashPrice.washingCenter = it
        }
        if (carWashPrice.washingCenter == null) {
            status.status = 0
            status.message = String.format("Washing Center: %s doesn't exist", carWashPrice.washingCenterId)
            status.value = carWashPrice.washingCenterId
            return status
        }

        carBodyRepository.findByIdAndDeletedAtIsNull(carWashPrice.carBodyId!!).ifPresent { carBody ->
            carWashPrice.carBody = carBody
        }
        if (carWashPrice.carBody == null) {
            status.status = 0
            status.message = String.format("Car Body: %s doesn't exist", carWashPrice.washingCenterId)
            status.value = carWashPrice.carBodyId
            return status
        }
        repository.save(carWashPrice)
        status.status = 1
        status.message = String.format("Car Wash Box: %s has been created", carWashPrice.cost.toString() + carWashPrice.carBodyId.toString())
        status.value = carWashPrice.id
        log.info(String.format("Car Wash Box: %s has been created", carWashPrice.cost.toString() + carWashPrice.carBodyId.toString()))
        return status
    }

    override fun editCarWashPrice(carWashPrice: CarWashPrice, carWashPriceId: String): Status {
        val status = Status()
        status.status = 1
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(carWashPriceId) ).ifPresentOrElse({

            washingCenterRepository.findByIdAndDeletedAtIsNull(carWashPrice.washingCenterId!!).ifPresent { washingCenter ->
                carWashPrice.washingCenter = washingCenter
            }
            if (carWashPrice.washingCenter == null) {
                status.status = 0
                status.message = String.format("Washing Center: %s doesn't exist", carWashPrice.washingCenterId)
                status.value = carWashPrice.washingCenterId
            }
            it.washingCenterId = carWashPrice.washingCenterId

            carBodyRepository.findByIdAndDeletedAtIsNull(carWashPrice.carBodyId!!).ifPresent { carBody ->
                carWashPrice.carBody = carBody
            }
            if (carWashPrice.carBody == null) {
                status.status = 0
                status.message = String.format("Car Body: %s doesn't exist", carWashPrice.washingCenterId)
                status.value = carWashPrice.carBodyId
            }
            it.carBodyId = carWashPrice.carBodyId
            it.cost = carWashPrice.cost
            if (status.status == 1){
                repository.save(it)
                status.status = 1
                status.message = String.format("Car Wash Price %s has been edited", it.id)
                status.value = it.id
            }

        }, {
            println("service2")
            status.message = String.format("Car Wash Price does not exist")
        })
        return status
    }

    override fun deleteCarWashPrice(id: UUID): Status {
        val status = Status()
        status.message = String.format("Car Wash Price %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { carWashBox ->
                carWashBox.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(carWashBox)

                status.status = 1
                status.message = String.format("Car Price %s has been deleted", id)
                log.info(String.format("Car Price %s has been deleted", id))
            }
        return status
    }

    override fun getCarWashPriceListByCarWashingCenter(
        params: MutableMap<String, String>,
        washingCenterId: UUID
    ): List<CarWashPrice> {
        return repository.findByWashingCenterIdAndDeletedAtIsNull(washingCenterId)
    }

    override fun getCarWashPriceById(id: UUID): Optional<CarWashPrice> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getCarWashPriceByCarBodyAndWashingCenter(washingCenterId: UUID, carBodyId: UUID): Optional<CarWashPrice> {
        return repository.findByWashingCenterIdAndCarBodyId(washingCenterId, carBodyId)
    }

}
