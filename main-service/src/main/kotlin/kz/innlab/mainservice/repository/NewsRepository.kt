package kz.innlab.mainservice.repository

import kz.innlab.mainservice.model.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface NewsRepository: JpaRepository<News, UUID>, JpaSpecificationExecutor<News> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<News>
    fun findAllByDeletedAtIsNull(): List<News>
}
