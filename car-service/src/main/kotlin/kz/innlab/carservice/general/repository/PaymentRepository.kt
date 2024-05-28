package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Cars
import kz.innlab.carservice.general.model.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface PaymentRepository: JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Payment>
    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<Payment>
}
