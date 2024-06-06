package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.CarWashWorker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CarWashWorkerRepository: JpaRepository<CarWashWorker, UUID>, JpaSpecificationExecutor<CarWashWorker> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<CarWashWorker>
    fun findAllByWashingCenterId(id: UUID): List<CarWashWorker>
    fun findAllByIdInAndDeletedAtIsNull(carWashWorkerIds: List<UUID>): List<CarWashWorker>
    fun findAllByDeletedAtIsNull(): List<CarWashWorker>
}
