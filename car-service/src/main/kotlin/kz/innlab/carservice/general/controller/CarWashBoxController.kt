package kz.innlab.carWashBoxService.car.controller

import kz.innlab.carservice.car.service.CarWashBoxService
import kz.innlab.carservice.general.model.CarWashBox
import kz.innlab.carservice.general.dto.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/box")
class CarWashBoxController {
    @Autowired
    lateinit var carWashBoxService: CarWashBoxService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCarWashBox(@RequestBody carWashBox: CarWashBox, principal: Principal): Status {
        return carWashBoxService.createCarWashBox(carWashBox)
    }

    @PutMapping("/edit/{CarWashBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun editCarWashBox(@RequestBody carWashBox: CarWashBox, @PathVariable CarWashBoxId: String, principal: Principal): Status {
        return carWashBoxService.editCarWashBox(carWashBox, CarWashBoxId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCarWashBox(@PathVariable id: UUID, principal: Principal): Status {
        return carWashBoxService.deleteCarWashBox(id)
    }

    @GetMapping("/list/{carWashBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun getCarWashBoxListMy(@RequestParam params: MutableMap<String, String>, principal: Principal, @PathVariable carWashBoxId: UUID): List<CarWashBox> {
        return carWashBoxService.getCarWashBoxList(params, carWashBoxId)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarWashBoxById(@PathVariable id: UUID, principal: Principal): Optional<CarWashBox> {
        return carWashBoxService.getCarWashBoxById(id)
    }
}
