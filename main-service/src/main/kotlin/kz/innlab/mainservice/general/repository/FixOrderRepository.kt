package kz.innlab.mainservice.general.repository

import kz.innlab.mainservice.general.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

interface FixOrderRepository: JpaRepository<FixOrder, UUID>, JpaSpecificationExecutor<FixOrder> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<FixOrder>
    fun findAllByIdInAndDeletedAtIsNull(carWashBoxIds: List<UUID>): List<FixOrder>
    fun findAllByDeletedAtIsNull(): List<FixOrder>
    fun findByCarFixBoxIdAndDateTime(carWashBoxId: UUID, date: Date): List<FixOrder>
    // Custom query to retrieve FixOrders by carWashBoxId and date (ignoring time)
    @Query("SELECT o FROM FixOrder o WHERE o.carFixBoxId = :carFixBoxId AND FUNCTION('date', o.dateTime) = :date")
    fun findByCarFixBoxIdAndDate(
        @Param("carFixBoxId") carFixBoxId: UUID,
        @Param("date") date: LocalDate
    ): List<FixOrder>
}
