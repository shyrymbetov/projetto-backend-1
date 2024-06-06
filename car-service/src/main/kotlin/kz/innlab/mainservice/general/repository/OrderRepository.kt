package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

interface OrderRepository: JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Order>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<Order>
    fun findAllByDeletedAtIsNull(): List<Order>
    fun findByCarWashBoxIdAndDateTime(carWashBoxId: UUID, date: Date): List<Order>
    // Custom query to retrieve orders by carWashBoxId and date (ignoring time)
    @Query("SELECT o FROM Order o WHERE o.carWashBoxId = :carWashBoxId AND FUNCTION('date', o.dateTime) = :date")
    fun findByCarWashBoxIdAndDate(
        @Param("carWashBoxId") carWashBoxId: UUID,
        @Param("date") date: LocalDate
    ): List<Order>
}
