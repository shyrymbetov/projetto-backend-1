package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.OrderStatus
import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.FixOrder
import java.sql.Date
import java.util.*

interface FixOrderService {
    fun createOrder(order: FixOrder, userId: String): Status
    fun editOrder(order: FixOrder, orderId: String): Status
    fun editOrderStatus(orderStatus: OrderStatus, orderId: String): Status
    fun deleteOrder(id: UUID, userId: String): Status
    fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<FixOrder>
    fun getOrderById(id: UUID): Optional<FixOrder>
    fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<FixOrder>
    fun getOrderByDateAndWashingCenterId(washingCenterId: String, date: Date): Any
}
