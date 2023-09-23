package kz.innlab.fileservice.controller

import kz.innlab.fileservice.service.BookFileService
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/onlyoffice-callback")
class BookFileServlet : HttpServlet() {

    @Autowired
    lateinit var bookFileService: BookFileService

    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val writer = response.writer
        println("ugwyehrbpgiuwhergoihb")
        val fileIdString = request.getParameter("fileId")

        if (fileIdString == null) {
            // Handle the case when fileId is missing or empty
            writer.write("{\"error\":1, \"message\":\"fileId is missing or empty\"}")
        }

        val fileModel = bookFileService.getFile(UUID.fromString(fileIdString))
        val scanner: Scanner = Scanner(request.inputStream).useDelimiter("\\A")
        val body = if (scanner.hasNext()) scanner.next() else ""
        val jsonObj = JSONParser().parse(body) as JSONObject

        if (jsonObj["status"] as Long == 2L) {
            val downloadUri = jsonObj["url"] as String
            val url = URL(downloadUri)
            val connection = url.openConnection() as HttpURLConnection
            val stream = connection.inputStream
            val savedFile = File(bookFileService.getFullPath(fileModel.get()))
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
        writer.write("{\"error\":0}")
    }
}
