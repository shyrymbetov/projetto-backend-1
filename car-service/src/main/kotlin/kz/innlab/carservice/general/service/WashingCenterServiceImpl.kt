package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.UserWashingCenter
import kz.innlab.carservice.general.model.WashingCenter
import kz.innlab.carservice.general.repository.UserWashingCenterRepository
import kz.innlab.carservice.general.repository.WashingCenterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class WashingCenterServiceImpl : WashingCenterService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: WashingCenterRepository

    @Autowired
    lateinit var userWashingCenterRepository: UserWashingCenterRepository

    //    @Autowired
    //    lateinit var fileServiceClient: FileServiceClient


    override fun createWashingCenter(washingCenter: WashingCenter, userId: String): Status {
        val status = Status()

        washingCenter.employee = UUID.fromString(userId)

        repository.save(washingCenter)

        status.status = 1
        status.message = String.format("Washing Center: %s has been created", washingCenter.name)
        status.value = washingCenter.id
        log.info(String.format("Washing Center: %s has been created", washingCenter.name))
        return status
    }

    override fun editWashingCenter(washingCenter: WashingCenter, washingCenterId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(washingCenterId) ).ifPresentOrElse({
            it.name = washingCenter.name
            it.location = washingCenter.location
            it.lat = washingCenter.lat
            it.lon = washingCenter.lon
            it.description = washingCenter.description
            it.phone = washingCenter.phone
            it.startTime = washingCenter.startTime
            it.endTime = washingCenter.endTime
            repository.save(it)
            status.status = 1
            status.message = String.format("Washing Center %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Washing Center does not exist")
        })
        return status
    }

    override fun deleteWashingCenter(id: UUID, userId: String): Status {
        val status = Status()
        status.message = String.format("Washing Center %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { car ->
                car.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(car)

                status.status = 1
                status.message = String.format("Washing Center %s has been deleted", id)
                log.info(String.format("Washing Center %s has been deleted", id))
            }
        return status
    }

    override fun getWashingCentersListMy(params: MutableMap<String, String>, employee: String): List<WashingCenter> {
        return repository.findAllByEmployeeAndDeletedAtIsNull(UUID.fromString(employee))
    }

    override fun getWashingCentersList(params: MutableMap<String, String>): List<WashingCenter> {
        return repository.findAllByDeletedAtIsNull()
    }

    override fun getWashingCenterById(id: UUID): Optional<WashingCenter> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getListWashingCentersByIds(ids: List<UUID>): List<WashingCenter> {
        val result: MutableList<WashingCenter> = mutableListOf()
        repository.findAllByIdInAndDeletedAtIsNull(ids).map {
            result.add(it)
        }

        return result
    }

    override fun getMyFavoriteWashingCenter(userId: String): List<UserWashingCenter> {
        return userWashingCenterRepository.findAllByUserId(UUID.fromString(userId))
    }

    override fun addFavoriteWashingCenter(id: UUID, userId: String): Status {
        val userWashingCenter = UserWashingCenter()
        userWashingCenter.userId = UUID.fromString(userId)
        userWashingCenter.washingCenterId = id
        userWashingCenterRepository.save(userWashingCenter)

        return Status(1, "Successfully added to favorite")
    }

    override fun unFavoriteWashingCenter(id: UUID): Status {
        userWashingCenterRepository.deleteById(id)
        return Status(1, "Successfully deleted from favorite")
    }


}
