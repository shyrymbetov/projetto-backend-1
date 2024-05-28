package kz.innlab.carservice.general.controller


import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Order
import kz.innlab.carservice.general.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@RestController
@RequestMapping("/order")
class OrderController {

    @Autowired
    lateinit var orderService: OrderService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createOrder(@RequestBody order: Order, principal: Principal): Status {
        return orderService.createOrder(order, principal.name)
    }

    @PutMapping("/edit/{orderId}")
    @PreAuthorize("isAuthenticated()")
    fun editOrder(@RequestBody order: Order, @PathVariable orderId: String, principal: Principal): Status {
        return orderService.editOrder(order, orderId)
    }

    @PutMapping("/edit/status/{orderId}")
    @PreAuthorize("isAuthenticated()")
    fun editOrderStatus(@RequestBody orderStatus: OrderStatusEnum, @PathVariable orderId: String, principal: Principal): Status {
        return orderService.editOrderStatus(orderStatus, orderId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteOrder(@PathVariable id: UUID, principal: Principal): Status {
        return orderService.deleteOrder(id, principal.name)
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    fun getOrdersListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<Order> {
        return orderService.getOrdersListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getOrderById(@PathVariable id: UUID, principal: Principal): Optional<Order> {
        return orderService.getOrderById(id)
    }

    @PostMapping("/by-date-and-box/{carWashBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun getOrderByDateAndCarWashBoxId(
        @RequestBody(required = true) date: String,
        principal: Principal, @PathVariable carWashBoxId: String
    ): List<Order> {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val utilDate = dateFormat.parse(date)

            val sqlDate = Date(utilDate.time)

            return orderService.getOrderByDateAndCarWashBoxId(carWashBoxId, sqlDate)
        } catch (e: ParseException) {
            System.out.println("Error parsing date: " + e.message)
            return emptyList()
        }

    }

    @PostMapping("/by-date-and-center/{carWashBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun getOrderByDateAndWashingCenterId(
        @RequestBody(required = true) date: String,
        principal: Principal, @PathVariable carWashBoxId: String
    ): Any {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val utilDate = dateFormat.parse(date)

        val sqlDate = Date(utilDate.time)

        return orderService.getOrderByDateAndWashingCenterId(carWashBoxId, sqlDate)

    }
}
