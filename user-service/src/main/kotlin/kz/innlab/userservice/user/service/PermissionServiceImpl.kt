package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.*
import kz.innlab.userservice.user.model.Permission
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.repository.PermissionRepository
import kz.innlab.userservice.user.repository.PermissionSpecification.Companion.chapterEqual
import kz.innlab.userservice.user.repository.PermissionSpecification.Companion.deletedAtIsNull
import kz.innlab.userservice.user.repository.PermissionSpecification.Companion.moduleEqual
import kz.innlab.userservice.user.repository.RoleRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.*

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
@Service
class PermissionServiceImpl : PermissionService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: PermissionRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun getById(id: UUID, userId: String): Optional<Permission> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getList(params: MutableMap<String, String>, userId: String): List<Permission> {
        return repository.findAll(
            deletedAtIsNull()
                .and(moduleEqual(params["module"]))
                .and(chapterEqual(params["chapter"]))
        )
    }

    override fun getPageable(page: Pageable, params: MutableMap<String, String>, userId: String): Page<Permission> {
        return repository.findAll(
            deletedAtIsNull()
                .and(moduleEqual(params["module"]))
                .and(chapterEqual(params["chapter"])),
            page
        )
    }

    override fun create(item: Permission, userId: String): Status {
        val result = Status()
        item.eventLog!!.add(eventLog(item, "create", userId))
        if (!repository.existsByRoleIdAndModuleAndChapterAndDeletedAtIsNull(item.roleId!!, item.module, item.chapter)) {
            repository.save(item)
            result.status = 1
            result.message = "OK"
            result.value = item.id
        } else {
            result.message = "Exists"
        }

        return result
    }

    override fun edit(item: Permission, userId: String): Status {
        val result = Status()
        if (item.id != null) {
            repository.findById(item.id!!).ifPresentOrElse({
                item.eventLog!!.addAll(it.eventLog!!)
                item.eventLog!!.add(eventLog(it, "edit", userId))

                repository.save(item)
                result.status = 1
                result.message = "OK"
                result.value = item.id
            }, {
                result.status = 0
                result.message = "Not found"
            })
        } else {
            result.status = 0
            result.message = "Id is null!"
        }

        return result
    }

    override fun edit(crud: CrudDto, userId: String): Status {
        val result = Status()
        if (crud.id != null && crud.type != null && crud.value != null) {
            repository.findById(crud.id!!).ifPresentOrElse({
                var changed = false

                when (crud.type) {
                    CrudEnum.CREATE -> {
                        if (it.create != crud.value!!) {
                            it.create = crud.value!!
                            changed = true
                        }
                    }
                    CrudEnum.READ -> {
                        if (it.read != crud.value!!) {
                            it.read = crud.value!!
                            changed = true
                        }
                    }
                    CrudEnum.UPDATE -> {
                        if (it.update != crud.value!!) {
                            it.update = crud.value!!
                            changed = true
                        }
                    }
                    CrudEnum.DELETE -> {
                        if (it.delete != crud.value!!) {
                            it.delete = crud.value!!
                            changed = true
                        }
                    }
                    else -> {}
                }

                if (changed) {
                    it.eventLog!!.add(eventLog(it, "edit ${crud.type!!.name}", userId))
                }

                repository.save(it)
                result.status = 1
                result.message = "OK"
                result.value = it
            }, {
                result.status = 0
                result.message = "Not found"
            })
        } else {
            result.status = 0
            result.message = "Id is null!"
        }

        return result
    }

    override fun delete(id: UUID, userId: String): Status {
        val result = Status()
        repository.findById(id).ifPresentOrElse({
            val eventLog = eventLog(it, "delete", userId)
            it.eventLog!!.add(eventLog(it, "delete", userId))

            it.deletedAt = eventLog.date
            repository.save(it)

            result.status = 1
            result.message = "OK"
            result.value = id
        }, {
            result.status = 0
            result.message = "Not found"
        })

        return result
    }

    override fun delete(ids: List<UUID>, userId: String): Status {
        val result = Status()
        val deleted: MutableList<UUID> = mutableListOf()
        repository.findAllByIdInAndDeletedAtIsNull(ids).forEach {
            val status = delete(it.id!!, userId)
            if (status.status == 1) {
                deleted.add(it.id!!)
            }
        }
        result.status = if (deleted.size == ids.size) {
            1
        } else if (deleted.isNotEmpty()) {
            2
        } else {
            0
        }
        result.message = "OK"
        result.value = deleted
        return result
    }

    override fun getPermissions(value: PermissionDTO): MutableMap<String, Int> {
        var result = mutableMapOf("create_per" to 0, "read_per" to 0, "update_per" to 0, "delete_per" to 0)
        userService.getUserById(value.userId!!).ifPresent {
            val roles = if (it.roles.isNotEmpty()) {
                it.roles
            } else arrayListOf("GUEST")
            if (roles.contains("ADMIN")) {
                result = mutableMapOf("create_per" to 1, "read_per" to 1, "update_per" to 1, "delete_per" to 1)
                return@ifPresent
            }
            val roleIds = roleRepository.findAllByNameIgnoreCaseInAndDeletedAtIsNull(roles)
                .map { role -> role.id!! }
            if (roleIds.isNotEmpty()) {
                result = repository.getPermissions(roleIds, value.module!!, value.chapter!!)
            }
        }
        return result
    }

    private fun eventLog(item: Permission, title: String, userId: String): EventLogs {
        val eventLog = EventLogs()
        eventLog.title = "${title.uppercase()} Permission"
        eventLog.description = item.toString()
        eventLog.subjectId = "Permission"
        eventLog.objectId = userId
        return eventLog
    }
}
