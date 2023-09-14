package kz.innlab.bookservice.content.model

import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_abstacts")
@Builder
class BookAbstracts: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var bookId: UUID? = null
    var userId: UUID? = null

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String? = null

    var page: String? = null
}
