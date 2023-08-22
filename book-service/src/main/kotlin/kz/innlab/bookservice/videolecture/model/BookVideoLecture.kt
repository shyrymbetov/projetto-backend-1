package kz.innlab.bookservice.videolecture.model

import kz.innlab.bookservice.videolecture.model.payload.VideoLectureType
import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_video_lecture")
@Builder
class BookVideoLecture: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var bookId: UUID? = null

    var name: String? = null
    var description: String? = null

    @Enumerated(EnumType.STRING)
    var type: VideoLectureType? = null

    var fileId: UUID? = null

    var link: String? = null


}
