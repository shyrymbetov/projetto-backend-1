package kz.innlab.carservice.general.service


import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Cars
import kz.innlab.carservice.general.model.Payment
import kz.innlab.carservice.general.repository.CarBodyRepository
import kz.innlab.carservice.general.repository.CarRepository
import kz.innlab.carservice.general.repository.PaymentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class PaymentServiceImpl : PaymentService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: PaymentRepository
    override fun createPayment(payments: Payment, userId: String): Status {
        val status = Status()

        payments.userId = UUID.fromString(userId)
        println(payments.cvv)

        repository.save(payments)
        status.status = 1
        status.message = String.format("Payment: %s has been created", payments.owner)
        status.value = payments.owner
        log.info(String.format("Car: %s has been created", payments.owner))
        return status
    }

    override fun editPayment(payments: Payment, paymentId: String): Status {
        var status = Status()
        status.status = 1
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(paymentId) ).ifPresentOrElse({
            it.owner = payments.owner
            it.cvv = payments.cvv
            it.cardNumber = payments.cardNumber
            it.expiresDate = payments.expiresDate
            if (status.status == 1) {
                repository.save(it)
                status.status = 1
                status.message = String.format("Payment %s has been edited", it.id)
                status.value = it.id
            }

        }, {
            println("service2")
            status.message = String.format("Payment does not exist")
        })
        return status
    }

    override fun deletePayment(id: UUID, userId: String): Status {
        val status = Status()
        status.message = String.format("Payment %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { payment ->
                payment.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(payment)

                status.status = 1
                status.message = String.format("Payment %s has been deleted", id)
                log.info(String.format("Payment %s has been deleted", id))
            }
        return status
    }

    override fun getPaymentsListMy(params: MutableMap<String, String>, userId: String): List<Payment> {
        return repository.findAllByUserIdAndDeletedAtIsNull(UUID.fromString(userId))
    }

    override fun getPaymentById(id: UUID): Optional<Payment> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }


}