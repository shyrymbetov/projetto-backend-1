package kz.innlab.userservice.user.repository

import kz.innlab.userservice.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import kotlin.collections.ArrayList

interface UserRepository: JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<User>

    fun findAllByDeletedAtIsNull(): List<User>
    fun findAllByDeletedAtIsNull(page: Pageable): Page<User>
    fun findAllByIdInAndDeletedAtIsNull(@Param("id") ids: List<UUID>, page: Pageable): Page<User>
    fun findAllByIdInAndDeletedAtIsNull(@Param("id") ids: List<UUID>): List<User>
    fun findAllByIdInAndDeletedAtIsNotNull(@Param("id") ids: List<UUID>): List<User>

    fun findByEmail(@Param("email") email: String): List<User>

    fun findByEmailIgnoreCaseAndDeletedAtIsNull(@Param("email") email: String): Optional<User>

    @Query("SELECT users.* " +
            "FROM roles " +
            "RIGHT JOIN users_roles " +
            "ON roles.id = users_roles.role_id " +
            "RIGHT JOIN users " +
            "ON users_roles.user_id = users.id " +
            "WHERE priority_number BETWEEN :fromPriority AND :toPriority " +
            "AND roles.deleted_at IS NULL AND users.deleted_at IS NULL", nativeQuery = true)
    fun managerList(@Param("fromPriority") fromPriority: Long?, @Param("toPriority") toPriority: Long?): List<User>
}
