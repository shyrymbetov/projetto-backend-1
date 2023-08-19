package kz.innlab.fileservice.service

import kz.innlab.fileservice.model.payload.ImageResizeTypes
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID
import javax.imageio.ImageIO
import kotlin.math.abs
import kz.innlab.fileservice.model.File as FileModel
import kz.innlab.fileservice.service.FileService as MyFileService

@Service
class ImageService: MyFileService() {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getFullPath(fileModel: FileModel, type: ImageResizeTypes? = null): String {
        return super.getFullPath(fileModel) +
                if(type == null) { "" } else { "_${type.toString().lowercase()}" }
    }

    override fun saveFile(multipartFile: MultipartFile): UUID? {
        val fileId = super.saveFile(multipartFile)
        if (fileId != null) {
            fileRepository.findByIdAndDeletedAtIsNull(fileId).ifPresent {
                imageResizeAndSaveForAllTypes(it)
            }
        }
        return fileId
    }


    fun imageResizeAndSaveForAllTypes(image: FileModel) {
        for (value in ImageResizeTypes.values()) {
            try {
                imageResizeAndSaveByType(image, value)
            } catch (e: Exception) {
                log.error(e.message)
            }
        }
    }

    //Image resizing
    private fun imageResizeAndSaveByType(image: FileModel, type: ImageResizeTypes = ImageResizeTypes.SM) {
        val outputImagePath = getFullPath(image, type)

        // reads input image
        val inputImage: BufferedImage? = try {
            ImageIO.read(File(getFullPath(image)))
        } catch (e: Exception) {
            log.error(e.message)
            return
        }

        // get square size image center
        val imWidth = inputImage!!.width
        val imHeight = inputImage.height
        val size = minOf(imWidth, imHeight)

        var outputImage = if (imWidth > imHeight) {
            inputImage.getSubimage(abs(imWidth - imHeight) / 2, 0, size, size)
        } else {
            inputImage.getSubimage(0, abs(imWidth - imHeight) / 2, size, size)
        }

        if (size > type.scaledSize) {
            outputImage = resize(outputImage, type.scaledSize, type.scaledSize)
        }

//        ImageIO.write(outputImage, image.fileFormat, File(outputImagePath))
        ImageIO.write(outputImage, "png", File(outputImagePath))
        if (!File(outputImagePath).exists()) {
            throw FileNotFoundException("File doesn't created")
        }
    }

    private fun resize(img: BufferedImage, height: Int, width: Int): BufferedImage {
        val tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val resized = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = resized.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()
        return resized
    }

}
