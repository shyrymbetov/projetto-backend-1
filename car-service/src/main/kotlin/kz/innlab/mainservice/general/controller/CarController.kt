package kz.innlab.mainservice.general.controller



import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.Cars
import kz.innlab.mainservice.general.service.CarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
class CarController {

    @Autowired
    lateinit var carService: CarService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCar(@RequestBody cars: Cars, principal: Principal): Status {
        return carService.createCar(cars, principal.name)
    }

    @PutMapping("/edit/{carId}")
    @PreAuthorize("isAuthenticated()")
    fun editCar(@RequestBody cars: Cars, @PathVariable carId: String, principal: Principal): Status {
        return carService.editCar(cars, carId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteCar(@PathVariable id: UUID, principal: Principal): Status {
        return carService.deleteCar(id, principal.name)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getCarsListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<Cars> {
        return carService.getCarsListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getCarById(@PathVariable id: UUID, principal: Principal): Optional<Cars> {
        return carService.getCarById(id)
    }
}
