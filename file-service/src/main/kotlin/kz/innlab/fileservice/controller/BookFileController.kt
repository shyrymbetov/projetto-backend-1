package kz.innlab.fileservice.controller

import kz.innlab.fileservice.dto.Status
import kz.innlab.fileservice.service.BookFileService
import kz.innlab.fileservice.service.FileService
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
@RequestMapping("/book")
class BookFileController {

    @Autowired
    lateinit var bookFileService: BookFileService

    @GetMapping("/download/{id}")
    fun getDownloadFile(
        @PathVariable(value = "id") id: UUID,
        @RequestParam(value = "filename", required = false) filename: String? = "Word",
    ): File? {
        val file = bookFileService.getFile(id)
        if (file.isEmpty) {
            return null
        }

        val pathToFile = bookFileService.getFullPath(file.get())

        if (FileService.fileExists(pathToFile)) {
            return File(pathToFile)
        }

        return null
    }
    @PostMapping("/onlyoffice-callback")
    fun handleCallback(@RequestBody body: String): ResponseEntity<String> {
        // Your callback handling logic here
        val jsonObj: JSONObject = JSONParser().parse(body) as JSONObject
        println(body)
        val file = bookFileService.getFile(UUID.fromString(jsonObj["key"].toString()))
        // Parse the JSON data from OnlyOffice (assuming it's in JSON format)
        if (file.isEmpty) {
            return ResponseEntity.ok("{\"error\":0}")
        }
        val pathToFile = bookFileService.getFullPath(file.get())
        println(pathToFile);

        if (jsonObj["status"] as Long == 2L) {
            val downloadUri = jsonObj["url"] as String
            val url = URL(downloadUri)
            val connection = url.openConnection() as HttpURLConnection
            val stream = connection.inputStream
            val savedFile = File(pathToFile)
            FileOutputStream(savedFile).use { out ->
                var read: Int
                val bytes = ByteArray(1024)
                while (stream.read(bytes).also { read = it } != -1) {
                    out.write(bytes, 0, read)
                }
                out.flush()
            }
            connection.disconnect()
        }

        return ResponseEntity.ok("{\"error\":0}")
    }


    @PostMapping("/create")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun createEmptyFileForBook(): ResponseEntity<*> {
        return ResponseEntity(bookFileService.createEmptyFileForBook(), HttpStatus.OK)
    }

    @PostMapping("/onlyoffice/convert/{fileId}")
    fun convertFileToPdf(@PathVariable fileId: UUID): ResponseEntity<String> {
        // Your callback handling logic here
        val body = "{\"async\":false,\"filetype\":\"docx\",\"key\":\"$fileId\",\"outputtype\":\"pdf\",\"title\":\"ExampleDocumentTitle.docx\",\"url\":\"https://x.ozenx.io/bcspc/attachments/download/5301c90b-0c06-4b3e-86a0-8b9c024539ee\"}"



        // Return a response (for example, a success message)
        return ResponseEntity.ok("{\"error\":0}")
    }



    @PostMapping("/sketchfab/upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadSketchfabFile(
        @RequestParam("name") name: String? = "Name",
        @RequestParam("file") fileForm: MultipartFile
    ): ResponseEntity<*> {
        val status = Status()
        val result = bookFileService.uploadSketchfabFile(name, fileForm)
        if (result != null) {
            status.status = 1
            status.message = "OK"
            status.value = result
        }
        return ResponseEntity(status, HttpStatus.OK)
    }

    @GetMapping("/sketchfab")
    @PreAuthorize("isAuthenticated()")
    fun sketchfabFileList(
        @RequestParam search: String
    ): ResponseEntity<*> {
        return ResponseEntity(bookFileService.sketchfabFileList(search), HttpStatus.OK)
    }
}
