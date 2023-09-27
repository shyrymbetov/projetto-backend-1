package kz.innlab.fileservice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "office", url = "https://onlyoffice.mugalim.academy")
interface DocumentServerClient {

    @PostMapping("/ConvertService.ashx")
    fun getConvertedFileLink(@RequestBody body: String): String
}
