package kz.innlab.bookservice.system.client

import kz.innlab.bookservice.system.dto.PermissionDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "user-service")
interface UserServiceClient {

    @PostMapping("/users/permissions/crud")
    fun permission(@RequestBody item: PermissionDTO): MutableMap<String, Int>
}
