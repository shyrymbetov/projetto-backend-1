package kz.innlab.mainservice.system.service

import kz.innlab.mainservice.system.client.UserServiceClient
import kz.innlab.mainservice.system.dto.PermissionDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 08.02.2023
 */
@Service
class PermissionService {
    @Autowired
    lateinit var userClient: UserServiceClient

    fun permission(value: PermissionDTO, type: String): Boolean {
        val result = userClient.permission(value)
        return result.containsKey(type) && result[type]!! > 0
    }

    fun permission(userId: Any, chapter: String, type: String): Boolean {
        val result = userClient.permission(PermissionDTO(UUID.fromString(userId.toString()), chapter))
        val key = key(type)
        return result.containsKey(key) && result[key]!! > 0
    }

    fun permission(userId: Any, chapter: String, type: List<String>): Boolean {
        val access = userClient.permission(PermissionDTO(UUID.fromString(userId.toString()), chapter))
        var result = false
        type.forEach breaking@ {
            result = access.containsKey(key(it)) && access[key(it)]!! > 0
            if (result) {
                return@breaking
            }
        }
        return result
    }

    companion object {
        private fun key(type: String): String {
            return when (type) {
                "create_per", "create" -> "create_per"
                "read_per", "read" -> "read_per"
                "update_per", "update" -> "update_per"
                "delete_per", "delete" -> "delete_per"
                else -> ""
            }
        }
    }
}
