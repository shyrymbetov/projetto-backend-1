package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface PaymentRepository: JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Payment>
    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<Payment>
}
