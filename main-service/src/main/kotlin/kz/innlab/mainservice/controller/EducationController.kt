package kz.innlab.mainservice.controller

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Education
import kz.innlab.mainservice.service.EducationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/education")
class EducationController {

    @Autowired
    lateinit var educationService: EducationService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createEducation(@RequestBody education: Education, principal: Principal): Status {
        return educationService.createEducation(education)
    }

    @PutMapping("/edit/{educationId}")
    @PreAuthorize("isAuthenticated()")
    fun editEducation(@RequestBody education: Education, @PathVariable educationId: UUID, principal: Principal): Status {
        return educationService.editEducation(education, educationId)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteEducation(@PathVariable id: UUID, principal: Principal): Status {
        return educationService.deleteEducation(id)
    }

    @GetMapping("/list")
    fun getEducationList(): List<Education> {
        return educationService.getEducationList()
    }

    @GetMapping("/{id}")
    fun getEducationById(@PathVariable id: UUID): Optional<Education> {
        return educationService.getEducationById(id)
    }
}
