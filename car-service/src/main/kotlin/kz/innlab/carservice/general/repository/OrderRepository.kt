package kz.innlab.carservice.general.repository

import kz.innlab.carservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface OrderRepository: JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Order>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<Order>
    fun findAllByDeletedAtIsNull(): List<Order>
}
