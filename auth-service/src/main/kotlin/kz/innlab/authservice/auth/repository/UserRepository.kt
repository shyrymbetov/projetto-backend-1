package kz.innlab.authservice.auth.repository

import kz.innlab.authservice.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import kotlin.collections.ArrayList

interface UserRepository: JpaRepository<User, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<User>

    fun findAllByIdInAndDeletedAtIsNull(@Param("id") ids: List<UUID>): ArrayList<User>
    fun findAllByIdInAndDeletedAtIsNotNull(@Param("id") ids: List<UUID>): ArrayList<User>

    fun findByEmailIgnoreCaseAndDeletedAtIsNull(@Param("email") email: String): Optional<User>

    @Query("SELECT users.* " +
            "FROM roles " +
            "RIGHT JOIN users_roles " +
            "ON roles.id = users_roles.role_id " +
            "RIGHT JOIN users " +
            "ON users_roles.user_id = users.id " +
            "WHERE priority_number BETWEEN :fromPriority AND :toPriority " +
            "AND roles.deleted_at IS NULL AND users.deleted_at IS NULL", nativeQuery = true)
    fun managerList(@Param("fromPriority") fromPriority: Long?, @Param("toPriority") toPriority: Long?): ArrayList<User>
}
