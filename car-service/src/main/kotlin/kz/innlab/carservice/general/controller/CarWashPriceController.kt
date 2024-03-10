package kz.innlab.carservice.general.controller

import kz.innlab.carservice.car.service.CarWashPriceService
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarWashPrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/price")
class CarWashPriceController {
    @Autowired
    lateinit var carWashPriceService: CarWashPriceService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCarWashPrice(@RequestBody carWashPrice: CarWashPrice, principal: Principal): Status {
        return carWashPriceService.createCarWashPrice(carWashPrice)
    }

    @PutMapping("/edit/{CarWashBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun editCarWashPrice(@RequestBody carWashPrice: CarWashPrice, @PathVariable CarWashBoxId: String, principal: Principal): Status {
        return carWashPriceService.editCarWashPrice(carWashPrice, CarWashBoxId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCarWashPrice(@PathVariable id: UUID, principal: Principal): Status {
        return carWashPriceService.deleteCarWashPrice(id)
    }

    @GetMapping("/list/{carWashingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun getCarWashPriceListByCarWashingCenter(@RequestParam params: MutableMap<String, String>, principal: Principal, @PathVariable carWashingCenterId: UUID): List<CarWashPrice> {
        return carWashPriceService.getCarWashPriceListByCarWashingCenter(params, carWashingCenterId)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarWashPriceById(@PathVariable id: UUID, principal: Principal): Optional<CarWashPrice> {
        return carWashPriceService.getCarWashPriceById(id)
    }
}
