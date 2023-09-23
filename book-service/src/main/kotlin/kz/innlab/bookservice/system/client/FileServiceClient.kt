package kz.innlab.bookservice.system.client

import kz.innlab.bookservice.book.dto.Status
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "file-service")
interface FileServiceClient {

    @PostMapping("/file/book/create")
    fun createEmptyFileForBook(): Status
}
