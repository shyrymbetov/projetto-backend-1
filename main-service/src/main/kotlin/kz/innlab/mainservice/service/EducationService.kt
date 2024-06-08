package kz.innlab.mainservice.service

import kz.innlab.mainservice.model.Education
import kz.innlab.mainservice.dto.Status
import java.util.*

interface EducationService {
    fun createEducation(education: Education): Status
    fun editEducation(education: Education, educationId: UUID): Status
    fun deleteEducation(id: UUID): Status
    fun getEducationList(): List<Education>
    fun getEducationById(id: UUID): Optional<Education>
}
