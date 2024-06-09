package kz.innlab.mainservice.controller

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.News
import kz.innlab.mainservice.model.Work
import kz.innlab.mainservice.service.WorkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/work")
class WorkController {

    @Autowired
    lateinit var workService: WorkService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createWork(@RequestBody work: Work, principal: Principal): Status {
        return workService.createWork(work, UUID.fromString(principal.name))
    }

    @PutMapping("/edit/{workId}")
    @PreAuthorize("isAuthenticated()")
    fun editWork(@RequestBody work: Work, @PathVariable workId: UUID, principal: Principal): Status {
        return workService.editWork(work, workId)
    }

    @PostMapping("/like/{workId}")
    @PreAuthorize("isAuthenticated()")
    fun likeWork(@PathVariable workId: UUID, principal: Principal): Status {
        return workService.likeWork(workId, UUID.fromString(principal.name))
    }

    @PostMapping("/unlike/{workId}")
    @PreAuthorize("isAuthenticated()")
    fun unlikeWork(@PathVariable workId: UUID, principal: Principal): Status {
        return workService.unlikeWork(workId, UUID.fromString(principal.name))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteWork(@PathVariable id: UUID, principal: Principal): Status {
        return workService.deleteWork(id)
    }

    @GetMapping("/list")
    fun getWorkList(principal: Principal?): List<Work> {
        return workService.getWorkList(UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/{id}")
    fun getWorkById(@PathVariable id: UUID, principal: Principal?): Optional<Work> {
        return workService.getWorkById(id, UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/liked")
    @PreAuthorize("isAuthenticated()")
    fun getLikedNews(principal: Principal): List<Work> {
        return workService.getLikedWorks(UUID.fromString(principal.name))
    }
}
