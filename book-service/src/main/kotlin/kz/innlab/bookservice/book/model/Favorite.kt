package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "book_Favorite",
    uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("userId", "bookId"))]
)
class Favorite: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var userId: UUID? = null

    var bookId: UUID? = null
}
