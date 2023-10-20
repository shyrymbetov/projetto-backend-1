package kz.innlab.bookservice.progress.model

import kz.innlab.bookservice.system.model.Auditable
import kz.innlab.bookservice.test.model.TestQuestions
import java.security.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "book_statistics",
    uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("readerId", "bookId"))]
)
class BookStatistics: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var readerId: UUID? = null

    var bookId: UUID? = null

    @OneToMany(mappedBy = "bookStatistics")
    var bookProgress: List<BookProgress> = mutableListOf()

    var currentPage: String? = ""
}
