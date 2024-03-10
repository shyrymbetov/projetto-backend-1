package kz.innlab.carservice.car.service

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Order
import java.util.*

interface OrderService {
    fun createOrder(order: Order, userId: String): Status
    fun editOrder(order: Order, orderId: String): Status
    fun deleteOrder(id: UUID, userId: String): Status
    fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<Order>
    fun getOrderById(id: UUID): Optional<Order>
}
