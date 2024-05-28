package kz.innlab.carservice.general.service


import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.Order
import kz.innlab.carservice.general.repository.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

@Service
class OrderServiceImpl : OrderService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: OrderRepository

    @Autowired
    lateinit var carWashPriceRepository: CarWashPriceRepository

    @Autowired
    lateinit var carWashBoxRepository: CarWashBoxRepository

    @Autowired
    lateinit var carWashWorkerRepository: CarWashWorkerRepository

    @Autowired
    lateinit var carRepository: CarRepository

    @Autowired
    private lateinit var washingCenterRepository: WashingCenterRepository

    //    @Autowired
    //    lateinit var fileServiceClient: FileServiceClient

    override fun createOrder(order: Order, userId: String): Status {
        val status = Status()

        carRepository.findByIdAndDeletedAtIsNull(order.carId!!).ifPresent {
            order.car = it
        }
        if (order.car == null) {
            status.status = 0
            status.message = String.format("Car: %s doesn't exist", order.carId)
            status.value = order.carId
            return status
        }

        carWashBoxRepository.findByIdAndDeletedAtIsNull(order.carWashBoxId!!).ifPresent {
            order.carWashBox = it
        }
        if (order.carWashBox == null) {
            status.status = 0
            status.message = String.format("Car Wash box: %s doesn't exist", order.carWashBoxId)
            status.value = order.carWashPriceId
            return status
        }

        carWashWorkerRepository.findByIdAndDeletedAtIsNull(order.carWashWorkerId!!).ifPresent {
            order.carWashWorker = it
        }
        if (order.carWashBox == null) {
            status.status = 0
            status.message = String.format("Car Wash Worker: %s doesn't exist", order.carWashWorkerId)
            status.value = order.carWashWorkerId
            return status
        }

        carWashPriceRepository.findByIdAndDeletedAtIsNull(order.carWashPriceId!!).ifPresent {
            order.carWashPrice = it
        }
        if (order.carWashPrice == null) {
            status.status = 0
            status.message = String.format("Car Wash Price: %s doesn't exist", order.carWashPriceId)
            status.value = order.carWashPriceId
            return status
        }


        repository.save(order)
        status.status = 1
        status.message = String.format("Order: %s has been created", "order.id")
        status.value = "order.id"
        log.info(String.format("Order: %s has been created", "order.id"))
        return status
    }

    override fun editOrder(order: Order, orderId: String): Status {
        val status = Status()
        status.status = 1
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(orderId) ).ifPresentOrElse({
            carRepository.findByIdAndDeletedAtIsNull(order.carId!!).ifPresent { car ->
                it.car = car
            }

            if (it.car == null) {
                status.status = 0
                status.message = String.format("Car: %s doesn't exist", order.carId)
                status.value = order.carId
            }
            it.carId = order.carId

            carWashBoxRepository.findByIdAndDeletedAtIsNull(order.carWashBoxId!!).ifPresent { box ->
                it.carWashBox = box
            }
            if (it.carWashBox == null) {
                status.status = 0
                status.message = String.format("Car Wash box: %s doesn't exist", order.carWashBoxId)
                status.value = order.carWashBoxId
            }
            it.carWashBoxId = order.carWashBoxId

            carWashPriceRepository.findByIdAndDeletedAtIsNull(order.carWashPriceId!!).ifPresent { price ->
                it.carWashPrice = price
            }
            if (it.carWashPrice == null) {
                status.status = 0
                status.message = String.format("Car Wash Price: %s doesn't exist", order.carWashPriceId)
                status.value = order.carWashPriceId
            }
            it.carWashPriceId = order.carWashPriceId

            carWashWorkerRepository.findByIdAndDeletedAtIsNull(order.carWashPriceId!!).ifPresent { worker ->
                it.carWashWorker = worker
            }
            if (it.carWashWorker == null) {
                status.status = 0
                status.message = String.format("Car Wash Worker: %s doesn't exist", order.carWashPriceId)
                status.value = order.carWashPriceId
            }
            it.carWashWorkerId = order.carWashWorkerId

            it.dateTime = order.dateTime
            it.status = order.status

            if (status.status == 1){
                repository.save(it)
                status.status = 1
                status.message = String.format("Washing Center %s has been edited", it.id)
                status.value = it.id
            }

        }, {
            println("service2")
            status.message = String.format("Washing Center does not exist")
        })
        return status
    }

    override fun editOrderStatus(orderStatus: OrderStatusEnum, orderId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(orderId) ).ifPresentOrElse({
            it.status = orderStatus
            repository.save(it)
            status.status = 1
            status.message = "Order status changed successfully!"
        }, {
            println("service2")
            status.message = String.format("Order type does not exist")
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
        return repository.findAllByDeletedAtIsNull().filter { it.car?.owner == UUID.fromString(userId) }
    }

    override fun getOrderById(id: UUID): Optional<Order> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<Order> {
        return repository.findByCarWashBoxIdAndDate(UUID.fromString(carWashBoxId), date.toLocalDate())
    }

    override fun getOrderByDateAndWashingCenterId(washingCenterId: String, date: Date): Any {
        val listOfBoxIds = carWashBoxRepository.findAllByWashingCenterId(UUID.fromString(washingCenterId)).map { it.id!! }
        val map = mutableMapOf<String, List<Order>>() // Assuming Order is the type of elements in ordersByBox

        for (item: UUID in listOfBoxIds) {
            val ordersByBox = repository.findByCarWashBoxIdAndDate(item, date.toLocalDate())
            if (ordersByBox.isNotEmpty()) {
                val boxName = ordersByBox[0].carWashBox!!.name!!
                map[boxName] = ordersByBox
            }
        }

        val list: ArrayList<Map<String, List<Order>>> = ArrayList()
        map.forEach { (key, value) ->
            list.add(mapOf(key to value))
        }

        return map
    }


}
