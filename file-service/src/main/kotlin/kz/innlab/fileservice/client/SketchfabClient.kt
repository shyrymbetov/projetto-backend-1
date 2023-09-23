package kz.innlab.fileservice.client

import kz.innlab.fileservice.dto.ModelSearchResponse
import kz.innlab.fileservice.dto.SketchfabUploadModelResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@FeignClient(name = "sketchfab", url = "https://api.sketchfab.com/v3")
interface SketchfabClient {

    @GetMapping("/send")
    fun getSketchfabModels(@RequestParam("q") q: String): ModelSearchResponse


    @PostMapping(value = ["/models"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadModel(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam("name") name: String,
        @RequestParam("isPublished") isPublished: Boolean,
        @RequestParam("isInspectable") isInspectable: Boolean,
        @RequestPart("modelFile") modelFile: MultipartFile
    ): ResponseEntity<*>
}
