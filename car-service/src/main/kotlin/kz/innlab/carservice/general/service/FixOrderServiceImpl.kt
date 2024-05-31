package kz.innlab.carservice.general.service


import kz.innlab.carservice.general.dto.OrderStatus
import kz.innlab.carservice.general.dto.OrderStatusEnum
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.FixOrder
import kz.innlab.carservice.general.model.Order
import kz.innlab.carservice.general.repository.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.util.*

@Service
class FixOrderServiceImpl : FixOrderService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: FixOrderRepository

    @Autowired
    lateinit var carFixBoxRepository: CarFixBoxRepository

    @Autowired
    lateinit var carFixRepository: CarFixRepository

    @Autowired
    lateinit var carWashWorkerRepository: CarWashWorkerRepository

    @Autowired
    lateinit var carRepository: CarRepository

    //    @Autowired
    //    lateinit var fileServiceClient: FileServiceClient

    override fun createOrder(order: FixOrder, userId: String): Status {
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

        carFixRepository.findByIdAndDeletedAtIsNull(order.carFixId!!).ifPresent {
            order.carFix = it
        }
        if (order.carFix == null) {
            status.status = 0
            status.message = String.format("Car Fix: %s doesn't exist", order.carFixId)
            status.value = order.carFixId
            return status
        }

        carFixBoxRepository.findByIdAndDeletedAtIsNull(order.carFixBoxId!!).ifPresent {
            order.carFixBox = it
        }
        if (order.carFixBox == null) {
            status.status = 0
            status.message = String.format("Car Wash box: %s doesn't exist", order.carFixBoxId)
            status.value = order.carFixId
            return status
        }

        carWashWorkerRepository.findByIdAndDeletedAtIsNull(order.carWashWorkerId!!).ifPresent {
            order.carWashWorker = it
        }
        if (order.carFixBox == null) {
            status.status = 0
            status.message = String.format("Car Wash Worker: %s doesn't exist", order.carWashWorkerId)
            return status
        }

        repository.save(order)
        status.status = 1
        status.message = String.format("Order: %s has been created", "order.id")
        status.value = "order.id"
        log.info(String.format("Order: %s has been created", "order.id"))
        return status
    }

    override fun editOrder(order: FixOrder, orderId: String): Status {
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

            carFixBoxRepository.findByIdAndDeletedAtIsNull(order.carFixBoxId!!).ifPresent { box ->
                it.carFixBox = box
            }
            if (it.carFixBox == null) {
                status.status = 0
                status.message = String.format("Car Wash box: %s doesn't exist", order.carFixBoxId)
            }
            it.carFixBoxId = order.carFixBoxId

            carWashWorkerRepository.findByIdAndDeletedAtIsNull(order.carWashWorkerId!!).ifPresent { worker ->
                it.carWashWorker = worker
            }
            if (it.carWashWorker == null) {
                status.status = 0
                status.message = String.format("Car Wash Worker: %s doesn't exist", order.carWashWorkerId)
                status.value = order.carWashWorkerId
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

    override fun editOrderStatus(orderStatus: OrderStatus, orderId: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull( UUID.fromString(orderId) ).ifPresentOrElse({
            it.status = OrderStatusEnum.valueOf(orderStatus.status)
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

    override fun getOrdersListMy(params: MutableMap<String, String>, userId: String): List<FixOrder> {
        return repository.findAllByDeletedAtIsNull().filter { it.car?.owner == UUID.fromString(userId) }
    }

    override fun getOrderById(id: UUID): Optional<FixOrder> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getOrderByDateAndCarWashBoxId(carWashBoxId: String, date: Date): List<FixOrder> {
        return repository.findByCarFixBoxIdAndDate(UUID.fromString(carWashBoxId), date.toLocalDate())
    }

    override fun getOrderByDateAndWashingCenterId(washingCenterId: String, date: Date): Any {
        val listOfBoxIds = carFixBoxRepository.findAllByWashingCenterId(UUID.fromString(washingCenterId)).map { it.id!! }
        val map = mutableMapOf<String, List<FixOrder>>() // Assuming Order is the type of elements in ordersByBox

        for (item: UUID in listOfBoxIds) {
            val ordersByBox = repository.findByCarFixBoxIdAndDate(item, date.toLocalDate())
            if (ordersByBox.isNotEmpty()) {
                val boxName = ordersByBox[0].carFixBox!!.name!!
                map[boxName] = ordersByBox
            }
        }

        val list: ArrayList<Map<String, List<FixOrder>>> = ArrayList()
        map.forEach { (key, value) ->
            list.add(mapOf(key to value))
        }

        return map
    }


}
