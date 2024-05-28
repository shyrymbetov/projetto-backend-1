package kz.innlab.carservice.general.service

import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.FixOrder
import kz.innlab.carservice.general.model.Order
import java.sql.Date
import java.sql.Timestamp
import java.util.*

interface FixOrderService {
    fun createOrder(order: FixOrder, userId: String): Status
    fun editOrder(order: FixOrder, orderId: String): Status
    fun editOrderStatus(orderStatus: OrderStatusEnum, orderId: String): Status
    fun deleteOrder(id: UUID, userId: String): Status
    fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<FixOrder>
    fun getOrderById(id: UUID): Optional<FixOrder>
    fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<FixOrder>
    fun getOrderByDateAndWashingCenterId(washingCenterId: String, date: Date): Any
}
