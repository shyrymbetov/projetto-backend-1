package kz.innlab.carservice.general.controller

import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarFixBox
import kz.innlab.carservice.general.service.CarFixBoxService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/fix-box")
class CarFixBoxController {
    @Autowired
    lateinit var carFixBoxService: CarFixBoxService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCarFixBox(@RequestBody carFixBox: CarFixBox, principal: Principal): Status {
        return carFixBoxService.createCarFixBox(carFixBox)
    }

    @PutMapping("/edit/{carFixBoxId}")
    @PreAuthorize("isAuthenticated()")
    fun editCarFixBox(@RequestBody carFixBox: CarFixBox, @PathVariable carFixBoxId: String, principal: Principal): Status {
        return carFixBoxService.editCarFixBox(carFixBox, carFixBoxId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCarFixBox(@PathVariable id: UUID, principal: Principal): Status {
        return carFixBoxService.deleteCarFixBox(id)
    }

    @GetMapping("/list/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun getCarFixBoxListMy(@RequestParam params: MutableMap<String, String>, principal: Principal, @PathVariable washingCenterId: UUID): List<CarFixBox> {
        return carFixBoxService.getCarFixBoxList(params, washingCenterId)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarFixBoxById(@PathVariable id: UUID, principal: Principal): Optional<CarFixBox> {
        return carFixBoxService.getCarFixBoxById(id)
    }
}
