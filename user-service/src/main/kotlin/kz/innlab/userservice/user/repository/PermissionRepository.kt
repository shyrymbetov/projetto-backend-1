package kz.innlab.userservice.user.repository

import kz.innlab.userservice.user.model.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
interface PermissionRepository : JpaRepository<Permission, UUID>, JpaSpecificationExecutor<Permission> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Permission>
    fun findAllByIdIn(ids: List<UUID>): List<Permission>
    fun findAllByIdInAndDeletedAtIsNull(ids: List<UUID>): List<Permission>
    fun findAllByDeletedAtIsNull(): List<Permission>
    fun existsByRoleIdAndModuleAndChapterAndDeletedAtIsNull(roleId: UUID, module: String, chapter: String?): Boolean

    @Query("SELECT COALESCE(SUM(create_permission), 0) as create_per, " +
            "COALESCE(SUM(read_permission), 0) as read_per, " +
            "COALESCE(SUM(update_permission), 0) as update_per, " +
            "COALESCE(SUM(delete_permission), 0) as delete_per " +
            "FROM permissions " +
            "WHERE role_id IN (:roleIds)" +
            "AND module = :module AND chapter = :chapter", nativeQuery = true)
    fun getPermissions(roleIds: List<UUID>, module: String, chapter: String): MutableMap<String, Int>
}
