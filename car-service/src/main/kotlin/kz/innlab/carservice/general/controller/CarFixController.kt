package kz.innlab.carservice.general.controller


import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarFix
import kz.innlab.carservice.general.service.CarFixService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/car-fix")
class CarFixController {

    @Autowired
    lateinit var carFixService: CarFixService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCar(@RequestBody carFix: CarFix, principal: Principal): Status {
        return carFixService.createCarFix(carFix)
    }

    @PutMapping("/edit/{carFixId}")
    @PreAuthorize("isAuthenticated()")
    fun editCar(@RequestBody carFix: CarFix, @PathVariable carFixId: String, principal: Principal): Status {
        return carFixService.editCarFix(carFix, carFixId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCar(@PathVariable id: UUID, principal: Principal): Status {
        return carFixService.deleteCarFix(id)
    }

    @GetMapping("/list/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun getCarsListMy(@RequestParam params: MutableMap<String, String>, principal: Principal, @PathVariable washingCenterId: UUID): List<CarFix> {
        return carFixService.getCarFixList(params, washingCenterId)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarById(@PathVariable id: UUID, principal: Principal): Optional<CarFix> {
        return carFixService.getCarFixById(id)
    }
}
