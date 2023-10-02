package kz.innlab.fileservice.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kz.innlab.fileservice.client.DocumentServerClient
import kz.innlab.fileservice.dto.Status
import kz.innlab.fileservice.service.BookFileService
import kz.innlab.fileservice.service.FileService
import org.apache.poi.xwpf.usermodel.XWPFDocument
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

    @Autowired
    lateinit var documentServer: DocumentServerClient

    private val objectMapper = ObjectMapper()

    @GetMapping("/pdf/{id}")
    fun getDownloadFile(
        @PathVariable(value = "id") id: UUID,
        @RequestParam(value = "filename", required = false) filename: String? = "Word",
    ): ResponseEntity<*> {
        val file = bookFileService.getFile(id)
        val pathToFile = bookFileService.getPdfPath(file)
        if (FileService.fileExists(pathToFile)) {
            val resource = InputStreamResource(FileInputStream(pathToFile))

            val httpHeaders = HttpHeaders()
            val contentDisposition = ContentDisposition.builder("attachment")
                .filename("$filename.pdf", StandardCharsets.UTF_8)
                .build()
            httpHeaders.contentDisposition = contentDisposition
            httpHeaders.contentType = FileService.contentTypeFromMime("application/pdf", MediaType.APPLICATION_OCTET_STREAM)
            if (file.size != null && file.size!! > 0) {
                httpHeaders.contentLength = file.size!!
            }
            return ResponseEntity(resource, httpHeaders, HttpStatus.OK)
        }

        return ResponseEntity("File not exists", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/onlyoffice-callback/{fileId}")
    fun handleCallback(@RequestBody body: String, @PathVariable fileId: UUID): ResponseEntity<String> {
        // Parse the JSON data from OnlyOffice (assuming it's in JSON format)
        val jsonObj: JSONObject = JSONParser().parse(body) as JSONObject
        println(body)

        // Your callback handling logic here
        val file = bookFileService.getFile(fileId)
        val pathToFile = bookFileService.getFullPath(file)
        println(pathToFile)

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

            try {
                val document = XWPFDocument(FileInputStream(pathToFile))
                var content = arrayOf<String>()
                for (paragraph in document.paragraphs) {
                    if (paragraph.style != null) {
                        println(paragraph.text + " " + paragraph.style)
                        content = content.plus(paragraph.text)
                    }
                }
                bookFileService.editBookContent(fileId, content)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            convertFileToPdf(fileId)
            convertFileToHtml(fileId)
        }

        return ResponseEntity.ok("{\"error\":0}")
    }


    @PostMapping("/create")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun createEmptyFileForBook(): ResponseEntity<*> {
        return ResponseEntity(bookFileService.createEmptyFileForBook(), HttpStatus.OK)
    }

    fun convertFileToPdf(@PathVariable fileId: UUID): Status {
        // Your callback handling logic here
        val body = "{\"async\":false,\"filetype\":\"docx\",\"key\":\"${UUID.randomUUID()}\",\"outputtype\":\"pdf\",\"title\":\"convert.docx\",\"url\":\"https://ubooks.kz/soft/file/download/$fileId\"}"

        val jsonResponse: String = documentServer.getConvertedFileLink(body)
        println(jsonResponse)

        val xmlMapper = XmlMapper()

        // Parse the XML string into a JsonNode
        val jsonNode: JsonNode = xmlMapper.readTree(jsonResponse)

        // Convert the JsonNode to JSON string
        val jsonString = objectMapper.writeValueAsString(jsonNode)
        val resultMap = objectMapper.readValue(jsonString, MutableMap::class.java)

        // Your callback handling logic here
        val file = bookFileService.getFile(fileId)
        val pathToFile = bookFileService.getPdfPath(file)
        println(pathToFile)
        println(resultMap)

        if (resultMap["EndConvert"].toString().toBoolean()) {
            val downloadUri = resultMap["FileUrl"] as String
            val url = URL(downloadUri)
            val connection = url.openConnection() as HttpURLConnection
            val stream = connection.inputStream
            val savedFile = File(pathToFile)
            if (!savedFile.exists()) {
                savedFile.createNewFile()
            }
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

        // Return a response (for example, a success message)
        return Status(1, "Successs")
    }
    fun convertFileToHtml(@PathVariable fileId: UUID): Status {
        // Your callback handling logic here
        val body = "{\"async\":false,\"filetype\":\"docx\",\"key\":\"${UUID.randomUUID()}\",\"outputtype\":\"html\",\"title\":\"convert.docx\",\"url\":\"https://ubooks.kz/soft/file/download/$fileId\"}"

        val jsonResponse: String = documentServer.getConvertedFileLink(body)
        println(jsonResponse)

        val xmlMapper = XmlMapper()

        // Parse the XML string into a JsonNode
        val jsonNode: JsonNode = xmlMapper.readTree(jsonResponse)

        // Convert the JsonNode to JSON string
        val jsonString = objectMapper.writeValueAsString(jsonNode)
        val resultMap = objectMapper.readValue(jsonString, MutableMap::class.java)

        // Your callback handling logic here
        val file = bookFileService.getFile(fileId)
        val pathToFile = bookFileService.getHtmlPath(file)
        println(pathToFile)
        println(resultMap)

        if (resultMap["EndConvert"].toString().toBoolean()) {
            val downloadUri = resultMap["FileUrl"] as String
            val url = URL(downloadUri)
            val connection = url.openConnection() as HttpURLConnection
            val stream = connection.inputStream
            val savedFile = File(pathToFile)
            if (!savedFile.exists()) {
                savedFile.createNewFile()
            }
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

        // Return a response (for example, a success message)
        return Status(1, "Successs")
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
