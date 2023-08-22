package kz.innlab.bookservice.hyperlink.model

import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_hyperlink")
@Builder
class BookHyperlink: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var bookId: UUID? = null

    var link: String? = null
}
