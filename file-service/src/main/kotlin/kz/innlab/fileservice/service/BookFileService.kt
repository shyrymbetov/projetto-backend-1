package kz.innlab.fileservice.service

import kz.innlab.fileservice.client.BookClient
import kz.innlab.fileservice.client.SketchfabClient
import kz.innlab.fileservice.dto.ModelSearchResponse
import kz.innlab.fileservice.dto.SketchfabUploadModelResponse
import kz.innlab.fileservice.dto.Status
import kz.innlab.fileservice.exceptionHandler.NoSuchElementFoundException
import kz.innlab.fileservice.repository.FileRepository
import org.glassfish.jersey.client.ClientConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotAuthorizedException
import kotlin.collections.ArrayList
import kotlin.experimental.and
import kz.innlab.fileservice.model.File as FileModel

/**
 * @project edm-spring
 * @author Yerassyl Shyrymbetov on 12.02.2022
 */
@Service
class BookFileService {
    @Value("\${spring.servlet.multipart.location}")
    private val uploadPath: String = ""

    @Value("\${sketchfab.token}")
    private val sketchfabToken: String? = ""

    @Autowired
    lateinit var sketchfabClient: SketchfabClient

    @Autowired
    lateinit var bookClient: BookClient

    @Autowired
    lateinit var fileRepository: FileRepository

    fun getFile(id: UUID): FileModel {
        return fileRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { NoSuchElementFoundException("Invalid file provided => $id") }
    }

    fun getPdfPath(fileModel: FileModel): String {
        val directory = "$uploadPath/bookFiles"
        Files.createDirectories(Paths.get(directory))
        return "$directory/${fileModel.id}"
    }

    fun getEpubPath(fileModel: FileModel): String {
        val directory = "$uploadPath/epubFiles"
        Files.createDirectories(Paths.get(directory))
        return "$directory/${fileModel.id}"
    }

    fun getHtmlPath(fileModel: FileModel): String {
        val directory = "$uploadPath/htmlFiles"
        Files.createDirectories(Paths.get(directory))
        return "$directory/${fileModel.id}"
    }

    fun getFullPath(fileModel: FileModel): String {
        return getFilesPath(uploadPath, fileModel.createdAt) + fileModel.id
    }

    fun getFullPathWord(): String {
        return  "$uploadPath/word/demo.docx"
    }

    fun createEmptyFileForBook(): Status {
        val file = File(getFullPathWord())
        val hashFile = sha256Checksum(file)
        val newFile = FileModel()
        newFile.fileName = "Book"
        newFile.fileFormat = "docx"
        newFile.mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        newFile.hashCode = hashFile
        newFile.size = -1L

        fileRepository.save(newFile)

        if (newFile.id != null) {
            try {
                copyFileTo(file, newFile.id!!, newFile.createdAt)
            } catch (e: Exception) {
                println(e.message)
                throw FileNotFoundException("File doesn't created")
            }
        }

        return  Status(1, "Success", newFile.id!!)
    }

    @Throws(IOException::class)
    fun multipartFileToFile(
        multipart: MultipartFile,
        dir: String
    ): File {
        val filepath: Path = Paths.get(dir, "tmp_${UUID.randomUUID()}")
        Files.createDirectories(Paths.get(dir))
        multipart.transferTo(filepath)
        return File(filepath.toString())
    }

    fun sha256Checksum(file: File): String = getHashOfFiles("SHA-256", file)

    private fun getHashOfFiles(typeHash: String, input: File): String {
        val digest = MessageDigest.getInstance(typeHash)
        val fileInputStream = FileInputStream(input)

        val byteArray = ByteArray(1024)
        var bytesCount = 0

        while (fileInputStream.read(byteArray).also { bytesCount = it } != -1) {
            digest.update(byteArray, 0, bytesCount)
        }
        fileInputStream.close()
        val bytes = digest.digest()

        val sb = StringBuilder()
        for (i in bytes.indices) {
            sb.append(((bytes[i].and(0xff.toByte())) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }

    fun createFile(tmpFile: File, id: UUID, createdDate: Timestamp = Timestamp(System.currentTimeMillis())): String {
        val newPath = getFilesPath(uploadPath, createdDate) + id
        tmpFile.let { sourceFile ->
            if (!fileExists(newPath)) {
                sourceFile.copyTo(File(newPath))
            }
            sourceFile.delete()
        }
        if (!fileExists(newPath))
            throw FileNotFoundException("File doesn't created")
        return newPath
    }
    fun copyFileTo(tmpFile: File, id: UUID, createdDate: Timestamp = Timestamp(System.currentTimeMillis())): String {
        val newPath = getFilesPath(uploadPath, createdDate) + id
        tmpFile.let { sourceFile ->
            if (!fileExists(newPath)) {
                sourceFile.copyTo(File(newPath))
            }
        }
        if (!fileExists(newPath))
            throw FileNotFoundException("File doesn't created")
        return newPath
    }

    fun uploadSketchfabFile(name: String?, multipartFile: MultipartFile): Status {
        val authorization = "Token $sketchfabToken"

        try {
            val sketchfabUploadModelResponse = sketchfabClient.uploadModel(
                authorization,
                name?: "Model",
                true,
                true,
                multipartFile
            )

            println("Upload sucess!!! " + sketchfabUploadModelResponse.body)
            return Status(1, "Success")
        } catch (ex: NotAuthorizedException) {
            //call not authorized, possible TOKEN problem
            println("Invalid Token -> $sketchfabToken")
        } catch (ex: BadRequestException) {
            //Basic - 50MB per upload
            println("bad parameters")
        }
        return Status()
    }

    fun sketchfabFileList(search: String): ModelSearchResponse {
        return sketchfabClient.getSketchfabModels(search)
    }

    fun editBookContent(fileId: UUID, content: Array<String>): Status {
        return bookClient.editBookContent(fileId, content)
    }

    companion object {
        internal fun getFilesPath(pathFiles: String, createdDate: Timestamp): String {
            val directory = "$pathFiles/files/${SimpleDateFormat("yyyy/MM/dd").format(createdDate)}"
            Files.createDirectories(Paths.get(directory))
            return "$directory/"
        }

        fun fileExists(path: String): Boolean {
            return File(path).exists()
        }

        fun contentTypeFromMime(contentMime: String?, default: org.springframework.http.MediaType
        = org.springframework.http.MediaType.IMAGE_JPEG): org.springframework.http.MediaType {
            var contentType = default
            if (
                contentMime != null
                && contentMime.isNotEmpty()
                && contentMime.split("/").size == 2
            ) {
                val types = contentMime.split("/")
                contentType = org.springframework.http.MediaType(types[0], types[1])
            }
            return contentType
        }
    }
}
