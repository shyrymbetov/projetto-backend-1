package kz.innlab.carservice.general.controller


import kz.innlab.carservice.car.service.CarBodyService
import kz.innlab.carservice.car.service.OrderService
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
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

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteOrder(@PathVariable id: UUID, principal: Principal): Status {
        return orderService.deleteOrder(id, principal.name)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getOrdersListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<Order> {
        return orderService.getOrdersListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getOrderById(@PathVariable id: UUID, principal: Principal): Optional<Order> {
        return orderService.getOrderById(id)
    }
}
