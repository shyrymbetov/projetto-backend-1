package kz.innlab.mainservice.controller

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Resource
import kz.innlab.mainservice.service.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/resource")
class ResourceController {

    @Autowired
    lateinit var resourceService: ResourceService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createResource(@RequestBody resource: Resource, principal: Principal): Status {
        return resourceService.createResource(resource)
    }

    @PutMapping("/edit/{resourceId}")
    @PreAuthorize("isAuthenticated()")
    fun editResource(@RequestBody resource: Resource, @PathVariable resourceId: UUID, principal: Principal): Status {
        return resourceService.editResource(resource, resourceId)
    }

    @PostMapping("/like/{resourceId}")
    @PreAuthorize("isAuthenticated()")
    fun likeResource(@PathVariable resourceId: UUID, principal: Principal): Status {
        return resourceService.likeResource(resourceId, UUID.fromString(principal.name))
    }

    @PostMapping("/unlike/{resourceId}")
    @PreAuthorize("isAuthenticated()")
    fun unlikeResource(@PathVariable resourceId: UUID, principal: Principal): Status {
        return resourceService.unlikeResource(resourceId, UUID.fromString(principal.name))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteResource(@PathVariable id: UUID, principal: Principal): Status {
        return resourceService.deleteResource(id)
    }

    @GetMapping("/list")
    fun getResourceList(principal: Principal?): List<Resource> {
        return resourceService.getResourceList(UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/list/{type}")
    fun getResourceListByType(@PathVariable type: String, principal: Principal?): List<Resource> {
        return resourceService.getResourceByType(type, UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/{id}")
    fun getResourceById(@PathVariable id: UUID, principal: Principal?): Optional<Resource> {
        return resourceService.getResourceById(id, UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/liked")
    @PreAuthorize("isAuthenticated()")
    fun getLikedResource(principal: Principal): List<Resource> {
        return resourceService.getLikedResource(UUID.fromString(principal.name))
    }
}
