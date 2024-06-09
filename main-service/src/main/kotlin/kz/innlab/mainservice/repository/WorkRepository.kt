package kz.innlab.mainservice.repository

import kz.innlab.mainservice.model.News
import kz.innlab.mainservice.model.Work
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface WorkRepository: JpaRepository<Work, UUID>, JpaSpecificationExecutor<Work> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Work>
    fun findAllByDeletedAtIsNull(): List<Work>
    fun findByAuthorAndDeletedAtIsNull(author: UUID): List<Work>
    @Query(
        value = "SELECT * FROM work WHERE :uuid = ANY(liked_users)",
        nativeQuery = true
    )
    fun findAllByLikedUsersContains(@Param("uuid") uuid: UUID): List<Work>
}
