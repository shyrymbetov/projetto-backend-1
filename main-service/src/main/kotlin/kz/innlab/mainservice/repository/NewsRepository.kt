package kz.innlab.mainservice.repository

import kz.innlab.mainservice.model.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface NewsRepository: JpaRepository<News, UUID>, JpaSpecificationExecutor<News> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<News>
    fun findAllByDeletedAtIsNull(): List<News>
    @Query(
        value = "SELECT * FROM news WHERE :uuid = ANY(liked_users)",
        nativeQuery = true
    )
    fun findAllByLikedUsersContains(@Param("uuid") uuid: UUID): List<News>
}
