package kz.innlab.bookservice.test.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import kz.innlab.bookservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_test_user")
@TypeDefs(
    TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType::class
    )
)
class BookTestUser: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var userId: UUID? = null

    var testId: UUID? = null

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var questions: Array<TestQuestionWithUserAnswer> = arrayOf()
}
