package kz.innlab.mainservice.repository

import kz.innlab.mainservice.model.Resource
import kz.innlab.mainservice.model.Work
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ResourceRepository: JpaRepository<Resource, UUID>, JpaSpecificationExecutor<Resource> {
    fun findAllByType(type: String): List<Resource>
    @Query(
        value = "SELECT * FROM resources WHERE :uuid = ANY(liked_users)",
        nativeQuery = true
    )
    fun findAllByLikedUsersContains(@Param("uuid") uuid: UUID): List<Resource>
}
