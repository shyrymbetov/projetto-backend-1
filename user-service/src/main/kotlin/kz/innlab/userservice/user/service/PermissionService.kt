package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.CrudDto
import kz.innlab.userservice.user.dto.PermissionDTO
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.Permission
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
interface PermissionService {
    fun getById(id: UUID, userId: String = ""): Optional<Permission>
    fun getList(params: MutableMap<String, String> = mutableMapOf(), userId: String = ""): List<Permission>
    fun getPageable(
        page: Pageable,
        params: MutableMap<String, String> = mutableMapOf(),
        userId: String = ""
    ): Page<Permission>

    fun create(item: Permission, userId: String = ""): Status
    fun edit(item: Permission, userId: String = ""): Status
    fun edit(crud: CrudDto, userId: String = ""): Status
    fun delete(id: UUID, userId: String = ""): Status
    fun delete(ids: List<UUID>, userId: String = ""): Status
    fun getPermissions(value: PermissionDTO): MutableMap<String, Int>
}
