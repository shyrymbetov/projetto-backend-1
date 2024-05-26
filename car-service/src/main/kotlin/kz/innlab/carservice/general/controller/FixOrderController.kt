package kz.innlab.carservice.general.controller


import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.FixOrder
import kz.innlab.carservice.general.service.FixOrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@RestController
@RequestMapping("/fix-order")
class FixOrderController {

    @Autowired
    lateinit var fixOrderService: FixOrderService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createOrder(@RequestBody fixOrder: FixOrder, principal: Principal): Status {
        return fixOrderService.createOrder(fixOrder, principal.name)
    }

    @PutMapping("/edit/{orderId}")
    @PreAuthorize("isAuthenticated()")
    fun editOrder(@RequestBody fixOrder: FixOrder, @PathVariable orderId: String, principal: Principal): Status {
        return fixOrderService.editOrder(fixOrder, orderId)
    }

    @PutMapping("/edit/status/{fixOrderId}")
    @PreAuthorize("isAuthenticated()")
    fun editOrderStatus(@RequestBody orderStatus: OrderStatusEnum, @PathVariable fixOrderId: String, principal: Principal): Status {
        return fixOrderService.editOrderStatus(orderStatus, fixOrderId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteOrder(@PathVariable id: UUID, principal: Principal): Status {
        return fixOrderService.deleteOrder(id, principal.name)
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    fun getOrdersListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<FixOrder> {
        return fixOrderService.getOrdersListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getOrderById(@PathVariable id: UUID, principal: Principal): Optional<FixOrder> {
        return fixOrderService.getOrderById(id)
    }
    @PostMapping("/by-date-and-box/{carFixBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun getFixOrderByDateAndCarFixBoxId(
        @RequestBody(required = true) date: String,
        principal: Principal, @PathVariable carFixBoxId: String
    ): List<FixOrder> {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val utilDate = dateFormat.parse(date)

            val sqlDate = Date(utilDate.time)

            return fixOrderService.getOrderByDateAndCarWashBoxId(carFixBoxId, sqlDate)
        } catch (e: ParseException) {
            println("Error parsing date: " + e.message)
            return emptyList()
        }

    }
}
