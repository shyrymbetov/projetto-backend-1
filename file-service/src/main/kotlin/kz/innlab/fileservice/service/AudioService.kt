package kz.innlab.fileservice.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.encode.AudioAttributes
import ws.schild.jave.encode.EncodingAttributes
import ws.schild.jave.info.MultimediaInfo
import java.io.File
import java.util.*

/**
 * @project microservice-template
 * @author Yerassyl Shyrymbetov on 08.02.2023
 */
@Service
class AudioService: FileService() {

    override fun saveFile(multipartFile: MultipartFile): UUID? {
        val fileId = super.saveFile(multipartFile)
        if (fileId != null) {
            fileRepository.findByIdAndDeletedAtIsNull(fileId).ifPresent {
                convertAudio(File(getFullPath(it)))
            }
        }
        return fileId
    }

    private fun convertAudio(source: File): Boolean {
        try {
            val target = File("/Users/bekzat/Music/converted/target.mp3")

            //Audio Attributes
            val audio = AudioAttributes()
            audio.setCodec("libmp3lame")
            audio.setBitRate(256000)
            audio.setChannels(2)
            audio.setSamplingRate(44100)

            val info = MultimediaInfo()

            //Encoding attributes
            val attrs = EncodingAttributes()
            attrs.setAudioAttributes(audio)

            val encoder = Encoder()
            encoder.encode(MultimediaObject(source), target, attrs)

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

}
