package kz.innlab.mainservice.general.service

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.Payment
import java.util.*

interface PaymentService {
    fun createPayment(payments: Payment, userId: String): Status
    fun editPayment(payments: Payment, paymentId: String): Status
    fun deletePayment(id: UUID, userId: String): Status
    fun getPaymentsListMy(params: MutableMap<String, String>, userId: String): List<Payment>
    fun getPaymentById(id: UUID): Optional<Payment>
}
