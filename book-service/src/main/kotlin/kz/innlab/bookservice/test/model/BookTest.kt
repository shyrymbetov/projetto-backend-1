package kz.innlab.bookservice.test.model

import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "book_test")
@Builder
class BookTest: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var bookId: UUID? = null

    var name: String? = null

    var duration: Long? = null

//    @Enumerated(EnumType.STRING)
    var complexity: String? = null

    @Transient
    var questions: List<TestQuestions> = listOf()
}
