package kz.innlab.carservice.car.controller


import kz.innlab.carservice.general.model.WashingCenter
import kz.innlab.carservice.car.service.WashingCenterService
import kz.innlab.carservice.general.dto.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/washing-center")
class WashingCenterController {
    @Autowired
    lateinit var washingCenterService: WashingCenterService

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun createWashingCenter(@RequestBody WashingCenter: WashingCenter, principal: Principal): Status {
        return washingCenterService.createWashingCenter(WashingCenter, principal.name)
    }

    @PutMapping("/edit/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun editWashingCenter(@RequestBody WashingCenter: WashingCenter, @PathVariable washingCenterId: String, principal: Principal): Status {
        return washingCenterService.editWashingCenter(WashingCenter, washingCenterId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteWashingCenter(@PathVariable id: UUID, principal: Principal): Status {
        return washingCenterService.deleteWashingCenter(id, principal.name)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getWashingCenterListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<WashingCenter> {
        return washingCenterService.getWashingCentersListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getWashingCenterById(@PathVariable id: UUID, principal: Principal): Optional<WashingCenter> {
        return washingCenterService.getWashingCenterById(id)
    }
}
