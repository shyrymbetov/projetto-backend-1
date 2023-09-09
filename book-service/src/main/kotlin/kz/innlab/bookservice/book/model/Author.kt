package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BOOK_AUTHORS")
class Author: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var fio: String? = null

    var userId: UUID? = null

    var bookId: UUID? = null

//    TODO numberBooks update when book created
    @Transient
    var numberBooks: Int = 0

}
