package kz.innlab.carservice.general.controller


import kz.innlab.carservice.general.model.WashingCenter
import kz.innlab.carservice.car.service.WashingCenterService
import kz.innlab.carservice.general.dto.Status
import kz.innlab.carservice.general.model.UserWashingCenter
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
    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('ADMIN')")
    fun createWashingCenter(@RequestBody washingCenter: WashingCenter, principal: Principal): Status {
        return washingCenterService.createWashingCenter(washingCenter, principal.name)
    }

    @PutMapping("/edit/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun editWashingCenter(@RequestBody washingCenter: WashingCenter, @PathVariable washingCenterId: String, principal: Principal): Status {
        return washingCenterService.editWashingCenter(washingCenter, washingCenterId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteWashingCenter(@PathVariable id: UUID, principal: Principal): Status {
        return washingCenterService.deleteWashingCenter(id, principal.name)
    }

    @GetMapping("/list-my")
    @PreAuthorize("isAuthenticated()")
    fun getWashingCenterListMy(@RequestParam params: MutableMap<String, String>, principal: Principal): List<WashingCenter> {
        return washingCenterService.getWashingCentersListMy(params, principal.name)
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getWashingCenterList(@RequestParam params: MutableMap<String, String>, principal: Principal): List<WashingCenter> {
        return washingCenterService.getWashingCentersList(params)
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getWashingCenterById(@PathVariable id: UUID, principal: Principal): Optional<WashingCenter> {
        return washingCenterService.getWashingCenterById(id)
    }


    @PostMapping("/washing-centers-by-ids")
    @PreAuthorize("isAuthenticated()")
    fun getListCoauthorsByIds(@RequestBody listOfCoauthorIds: List<UUID>, principal: Principal): List<WashingCenter> {
        return washingCenterService.getListWashingCentersByIds(listOfCoauthorIds)
    }

    @GetMapping("/favorite")
    @PreAuthorize("isAuthenticated()")
    fun getMyFavoriteWashingCenter(principal: Principal): List<UserWashingCenter> {
        return washingCenterService.getMyFavoriteWashingCenter(principal.name)
    }

    @PostMapping("/favorite/{washingCenterId}")
    @PreAuthorize("isAuthenticated()")
    fun favoriteWashingCenter( @PathVariable washingCenterId: String, principal: Principal): Status {
        return washingCenterService.addFavoriteWashingCenter(UUID.fromString(washingCenterId), principal.name)
    }

    @DeleteMapping("/unfavorite/{id}")
    @PreAuthorize("isAuthenticated()")
    fun unFavoriteWashingCenter( @PathVariable id: String, principal: Principal): Status {
        return washingCenterService.unFavoriteWashingCenter(UUID.fromString(id))
    }
}
