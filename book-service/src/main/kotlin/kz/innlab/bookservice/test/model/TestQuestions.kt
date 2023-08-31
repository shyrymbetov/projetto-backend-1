package kz.innlab.bookservice.test.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.bookservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_test_questions")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    ),
)
class TestQuestions: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var testId: UUID? = null

    var index: Int? = null

    var description: String? = null

    @Type(type = "string-array")
    @Column(columnDefinition = "character varying[]")
    var correctAnswers: Array<String> = arrayOf()

    @Type(type = "string-array")
    @Column(columnDefinition = "character varying[]")
    var wrongAnswers: Array<String> = arrayOf()

}
