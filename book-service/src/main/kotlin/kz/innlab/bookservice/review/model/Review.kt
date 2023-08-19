package kz.innlab.bookservice.review.model

import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import kz.innlab.bookservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "app_review")
@TypeDefs(
    TypeDef(
        name = "uuid-array",
        typeClass = UUIDArrayType::class
    )
)
class Review: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var userId: UUID? = null
    var bookId: UUID? = null
    var rating: Double? = null

    var title: String? = ""

    @Column(columnDefinition = "TEXT")
    var comment: String? = ""

    var status: String? = ""

    @Type(type = "uuid-array")
    @Column(columnDefinition = "uuid[]")
    var files: Array<UUID> = arrayOf()

}
