package kz.innlab.mainservice.general.controller

import kz.innlab.mainservice.general.dto.Status
import kz.innlab.mainservice.general.model.CarWashWorker
import kz.innlab.mainservice.general.service.CarWashWorkerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/worker")
class CarWashWorkerController {
    @Autowired
    lateinit var carWashWorkerService: CarWashWorkerService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createCarWashBox(@RequestBody carWashWorker: CarWashWorker, principal: Principal): Status {
        return carWashWorkerService.createCarWashWorker(carWashWorker)
    }

    @PutMapping("/edit/{carWashWorkerId}")
    @PreAuthorize("isAuthenticated()")
    fun editCarWashWorker(@RequestBody carWashWorker: CarWashWorker, @PathVariable carWashWorkerId: String, principal: Principal): Status {
        return carWashWorkerService.editCarWashWorker(carWashWorker, carWashWorkerId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deletecarWashWorker(@PathVariable id: UUID, principal: Principal): Status {
        return carWashWorkerService.deleteCarWashWorker(id)
    }

    @GetMapping("/list/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun getcarWashWorkerListMy(@RequestParam params: MutableMap<String, String>, principal: Principal, @PathVariable washingCenterId: UUID): List<CarWashWorker> {
        return carWashWorkerService.getCarWashWorkerList(params, washingCenterId)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getcarWashWorkerById(@PathVariable id: UUID, principal: Principal): Optional<CarWashWorker> {
        return carWashWorkerService.getCarWashWorkerById(id)
    }
}
