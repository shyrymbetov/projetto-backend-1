package kz.innlab.fileservice.controller

import kz.innlab.fileservice.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


/**
 * @author Bekzat Sailaubayev on 07.02.2023
 * @project microservice-template
 */
@RestController
@RequestMapping("/audiovideo")
class AudioVideoController {

    @Autowired
    lateinit var fileService: FileService

    @GetMapping("/video/{id}")
    fun streamVideo(
        @RequestHeader(value = "Range", required = false) httpRangeList: String?,
        @PathVariable(value = "id") id: UUID,
    ): Mono<ResponseEntity<ByteArray>> {
        val file = fileService.getFile(id)
        if (
            file.isEmpty
            || !file.get().mime!!.contains("video")
        ) {
            return Mono.just(ResponseEntity(null, HttpStatus.BAD_GATEWAY))
        }
        return Mono.just(getContent(file.get(), httpRangeList, "video"))
    }

    @GetMapping("/audio/{id}")
    fun streamAudioById(
        @RequestHeader(value = "Range", required = false) httpRangeList: String?,
        @PathVariable(value = "id") id: UUID,
    ): Mono<ResponseEntity<ByteArray>> {
        val file = fileService.getFile(id)
        if (
            file.isEmpty
            || !file.get().mime!!.contains("audio")
        ) {
            return Mono.just(ResponseEntity(null, HttpStatus.BAD_GATEWAY))
        }
        return Mono.just(getContent(file.get(), httpRangeList, "audio"))
    }


    private fun getContent(
        fileObject: kz.innlab.fileservice.model.File,
        range: String?,
        contentTypePrefix: String
    ): ResponseEntity<ByteArray> {
        var rangeStart: Long = 0
        var rangeEnd: Long
        val data: ByteArray
        val fileSize: Long
        val location = fileService.getFullPath(fileObject)
        try {
            fileSize = Optional.ofNullable(fileObject.fileName)
                .map { Paths.get(fileService.getFullPath(fileObject)) }
                .map { path: Path -> sizeFromFile(path) }
                .orElse(0L)
            if (range == null) {
                return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "$contentTypePrefix/${fileObject.fileFormat}")
                    .header("Content-Length", fileSize.toString())
                    .body(readByteRange(location, "", rangeStart, fileSize - 1))
            }
            val ranges = range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            rangeStart = ranges[0].substring(6).toLong()
            rangeEnd = if (ranges.size > 1) {
                ranges[1].toLong()
            } else {
                fileSize - 1
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1
            }
            data = readByteRange(location, "", rangeStart, rangeEnd)
        } catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
        val contentLength = (rangeEnd - rangeStart + 1).toString()
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .header(
                "Content-Type", "$contentTypePrefix/${fileObject.fileFormat}"
            )
            .header("Accept-Ranges", "bytes")
            .header("Content-Length", contentLength)
            .header("Content-Range", "bytes $rangeStart-$rangeEnd/$fileSize")
            .body(data)
    }

    @Throws(IOException::class)
    fun readByteRange(location: String, filename: String?, start: Long, end: Long): ByteArray {
        val path = Paths.get(location, filename)
        Files.newInputStream(path).use { inputStream ->
            ByteArrayOutputStream().use { bufferedOutputStream ->
                val data = ByteArray(BYTE_RANGE)
                var nRead: Int
                while (inputStream.read(data, 0, data.size).also { nRead = it } != -1) {
                    bufferedOutputStream.write(data, 0, nRead)
                }
                bufferedOutputStream.flush()
                val result = ByteArray((end - start).toInt() + 1)
                System.arraycopy(bufferedOutputStream.toByteArray(), start.toInt(), result, 0, result.size)
                return result
            }
        }
    }

    private fun sizeFromFile(path: Path): Long {
        try {
            return Files.size(path)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return 0L
    }

    companion object {
        const val VIDEO_PATH = "/static/videos"
        const val AUDIO_PATH = "/Users/bekzat/Music/test"
        const val BYTE_RANGE = 128 // increase the byterange from here
    }
}
