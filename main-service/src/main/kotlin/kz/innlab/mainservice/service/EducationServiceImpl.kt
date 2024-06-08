package kz.innlab.mainservice.service


import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Education
import kz.innlab.mainservice.repository.EducationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class EducationServiceImpl: EducationService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: EducationRepository
    override fun createEducation(education: Education): Status {
        val status = Status()

        repository.save(education)
        status.status = 1
        status.message = String.format("Education: %s has been created", education.titleEn)
        status.value = education.titleEn
        log.info(String.format("Education: %s has been created", education.titleEn))
        return status
    }

    override fun editEducation(education: Education, educationId: UUID): Status {
        val status = Status()
        repository.findById(educationId).ifPresentOrElse({
            it.titleEn = education.titleEn
            it.titleRu = education.titleRu
            it.contentEn = education.contentEn
            it.contentRu = education.contentRu
            it.icon = education.icon
            it.link = education.link
            it.image = education.image
            repository.save(it)
            status.status = 1
            status.message = String.format("Education %s has been edited", it.id)
            status.value = it.id
        }, {
            status.message = String.format("Education does not exist")
        })
        return status
    }

    override fun deleteEducation(id: UUID): Status {
        val status = Status()
        status.message = String.format("Education %s does not exist", id)
        repository.findById(id)
            .ifPresent { education ->
                repository.delete(education)
                status.status = 1
                status.message = String.format("Education %s has been deleted", id)
                log.info(String.format("Education %s has been deleted", id))
            }
        return status
    }

    override fun getEducationList(): List<Education> {
        return repository.findAll()
    }

    override fun getEducationById(id: UUID): Optional<Education> {
        return repository.findById(id)
    }
}
