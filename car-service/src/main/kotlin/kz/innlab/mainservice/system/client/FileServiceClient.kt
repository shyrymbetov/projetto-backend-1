package kz.innlab.mainservice.system.client

import kz.innlab.mainservice.general.dto.Status
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "file-service")
interface FileServiceClient {

    @PostMapping("/file/book/create")
    fun createEmptyFileForBook(): Status
}
