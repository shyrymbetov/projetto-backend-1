package kz.innlab.carservice.car.controller


import kz.innlab.carservice.car.service.CarBodyService
import kz.innlab.carservice.car.service.CarService
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.CarBody
import kz.innlab.carservice.general.model.Cars
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/carbody")
class CarBodyController {

    @Autowired
    lateinit var carBodyService: CarBodyService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCar(@RequestBody carBody: CarBody, principal: Principal): Status {
        return carBodyService.createCarBody(carBody)
    }

    @PutMapping("/edit/{carBodyId}")
    @PreAuthorize("isAuthenticated()")
    fun editCar(@RequestBody carBody: CarBody, @PathVariable carBodyId: String, principal: Principal): Status {
        return carBodyService.editCarBody(carBody, carBodyId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCar(@PathVariable id: UUID, principal: Principal): Status {
        return carBodyService.deleteCarBody(id)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getCarsListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<CarBody> {
        return carBodyService.getCarBodyList(params)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarById(@PathVariable id: UUID, principal: Principal): Optional<CarBody> {
        return carBodyService.getCarBodyById(id)
    }
}
