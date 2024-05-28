package kz.innlab.carservice.general.controller



import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Payment
import kz.innlab.carservice.general.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/payment")
class PaymentController {

    @Autowired
    lateinit var paymentService: PaymentService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createPayment(@RequestBody payments: Payment, principal: Principal): Status {
        return paymentService.createPayment(payments, principal.name)
    }

    @PutMapping("/edit/{paymentId}")
    @PreAuthorize("isAuthenticated()")
    fun editPayment(@RequestBody payments: Payment, @PathVariable paymentId: String, principal: Principal): Status {
        return paymentService.editPayment(payments, paymentId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deletePayment(@PathVariable id: UUID, principal: Principal): Status {
        return paymentService.deletePayment(id, principal.name)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getPaymentsListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<Payment> {
        return paymentService.getPaymentsListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getPaymentById(@PathVariable id: UUID, principal: Principal): Optional<Payment> {
        return paymentService.getPaymentById(id)
    }
}
