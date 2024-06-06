package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.OrderStatus
import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.Order
import java.sql.Date
import java.util.*

interface OrderService {
    fun createOrder(order: Order, userId: String): Status
    fun editOrder(order: Order, orderId: String): Status
    fun editOrderStatus(orderStatus: OrderStatus, orderId: String): Status
    fun deleteOrder(id: UUID, userId: String): Status
    fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<Order>
    fun getOrderById(id: UUID): Optional<Order>
    fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<Order>
    fun getOrderByDateAndWashingCenterId(washingCenterId: String, date: Date): Any
}
