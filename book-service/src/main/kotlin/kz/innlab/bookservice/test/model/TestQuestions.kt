package kz.innlab.bookservice.test.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
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
        name = "jsonb",
        typeClass = JsonBinaryType::class
    )
)
class TestQuestions: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var testId: UUID? = null

    var index: Int? = null

    var description: String? = null

    @Type(type = "jsonb")
    var correctAnswers: ArrayList<Answer> = arrayListOf()

    @Type(type = "jsonb")
    var wrongAnswers: ArrayList<Answer> = arrayListOf()

}
