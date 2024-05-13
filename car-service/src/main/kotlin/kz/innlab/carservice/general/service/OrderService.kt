package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Order
import java.sql.Date
import java.sql.Timestamp
import java.util.*

interface OrderService {
    fun createOrder(order: Order, userId: String): Status
    fun editOrder(order: Order, orderId: String): Status
    fun editOrderStatus(orderStatus: OrderStatusEnum, orderId: String): Status
    fun deleteOrder(id: UUID, userId: String): Status
    fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<Order>
    fun getOrderById(id: UUID): Optional<Order>
    fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<Order>
}
