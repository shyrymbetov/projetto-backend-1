package kz.innlab.mainservice.repository

import kz.innlab.mainservice.model.Education
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface EducationRepository: JpaRepository<Education, UUID>, JpaSpecificationExecutor<Education> {
}
