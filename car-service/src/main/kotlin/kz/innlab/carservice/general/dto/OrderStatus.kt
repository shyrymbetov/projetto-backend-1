package kz.innlab.carservice.general.dto

data class OrderStatus (
    var status: OrderStatusEnum = OrderStatusEnum.CREATED,
)
