package kz.innlab.carservice.general.service

import kz.innlab.carservice.car.service.OrderService
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Order
import kz.innlab.carservice.general.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class OrderServiceImpl : OrderService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: OrderRepository

    //    @Autowired
    //    lateinit var fileServiceClient: FileServiceClient

    override fun createOrder(order: Order, userId: String): Status {
        val status = Status()
        println(order.carWashPriceId)
        println(order.carWashBoxId)
        println(order.carId)
//        order.employee = UUID.fromString(userId)

//        repository.save(order)

        status.status = 1
        status.message = String.format("Order: %s has been created", "order.id")
        status.value = "order.id"
        log.info(String.format("Order: %s has been created", "order.id"))
        return status
    }

    override fun editOrder(order: Order, orderId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(orderId) ).ifPresentOrElse({
            // TODO editOrder
//            it.name = washingCenter.name
//            it.location = washingCenter.location
//            it.lat = washingCenter.lat
//            it.lon = washingCenter.lon
//            it.description = washingCenter.description
//            it.phone = washingCenter.phone
            repository.save(it)
            status.status = 1
            status.message = String.format("Washing Center %s has been edited", it.id)
            status.value = it.id
        }, {
            println("service2")
            status.message = String.format("Washing Center does not exist")
        })
        return status
    }

    override fun deleteOrder(id: UUID, userId: String): Status {
        val status = Status()
        status.message = String.format("Order %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { order ->
                order.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(order)

                status.status = 1
                status.message = String.format("Order %s has been deleted", id)
                log.info(String.format("Order %s has been deleted", id))
            }
        return status
    }

    override fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<Order> {
        TODO("Not yet implemented")
//        return repository.findAllByEmployeeAndDeletedAtIsNull(UUID.fromString(userId))
    }

    override fun getOrderById(id: UUID): Optional<Order> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }


}
