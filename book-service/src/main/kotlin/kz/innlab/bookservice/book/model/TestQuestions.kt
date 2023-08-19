package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_test_questions")
@Builder
class TestQuestions: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var testId: UUID? = null

    var index: Int? = null

    var description: String? = null


}
