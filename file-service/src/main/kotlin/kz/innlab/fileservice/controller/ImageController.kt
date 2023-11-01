package kz.innlab.fileservice.controller

import kz.innlab.fileservice.dto.Status
import kz.innlab.fileservice.model.payload.ImageResizeTypes
import kz.innlab.fileservice.service.FileService
import kz.innlab.fileservice.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @project edm-spring
 * @author Yerassyl Shyrymbetov on 12.02.2022
 */

@RestController
@RequestMapping("/image")
class ImageController {

    @Autowired
    lateinit var fileService: ImageService

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    fun getImageSource(
        @PathVariable(value = "id") id: UUID,
        @RequestParam size: String? = null,
    ): ResponseEntity<*> {
        val file = fileService.getFile(id)
        if (file.isEmpty) {
            return ResponseEntity("File not exists", HttpStatus.BAD_GATEWAY)
        }
        val pathToFile = fileService.getFullPath(file.get(), (if (size == null) null else ImageResizeTypes.valueOf(size.trim().uppercase())))
        if (FileService.fileExists(pathToFile)) {
            val resource = InputStreamResource(FileInputStream(pathToFile))

            val httpHeaders = HttpHeaders()
            val contentDisposition = ContentDisposition.builder("attachment")
                .filename("image-${UUID.randomUUID()}.${file.get().fileFormat}", StandardCharsets.UTF_8)
                .build()
            httpHeaders.contentDisposition = contentDisposition
            httpHeaders.contentType = FileService.contentTypeFromMime(file.get().mime, MediaType.IMAGE_JPEG)
            return ResponseEntity(resource, httpHeaders, HttpStatus.OK)
        }
        return ResponseEntity("File not exists", HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/get/{id}")
    fun getImageWithoutAuth(
        @PathVariable(value = "id") id: UUID,
        @RequestParam size: String? = null,
    ): ResponseEntity<*> {
        // TODO only images and webp
        return getImageSource(id, size)
    }

    @PostMapping("upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadFile(
        @RequestParam("file") fileForm: MultipartFile,
        authentication: Authentication
    ): ResponseEntity<*> {
        val status = Status()
        val result = fileService.saveFile(fileForm)
        if (result != null) {
            status.status = 1
            status.message = "OK"
            status.value = result
        }
        return ResponseEntity(status, HttpStatus.OK)
    }

}
