package kz.innlab.bookservice.test.model

import kz.innlab.bookservice.system.model.Auditable
import lombok.Builder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_test")
@Builder
class BookTest: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var bookId: UUID? = null

    var name: String? = null

//    @Enumerated(EnumType.STRING)
    var complexity: String? = null

    @OneToMany
    @JoinColumn(name = "testId", insertable = true, updatable = true)
    var questions: Set<TestQuestions> = setOf()
}
