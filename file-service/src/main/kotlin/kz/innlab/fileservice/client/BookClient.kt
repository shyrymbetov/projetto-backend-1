package kz.innlab.fileservice.client

import kz.innlab.fileservice.dto.Status
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(name = "book-service", path = "books")
interface BookClient {

    @PutMapping("/content/{fileId}")
    fun editBookContent(
        @PathVariable fileId: UUID,
        @RequestBody content: Array<String>
    ): Status
}
