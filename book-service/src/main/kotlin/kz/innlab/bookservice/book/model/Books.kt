package kz.innlab.bookservice.book.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import kz.innlab.bookservice.book.dto.BookStatusEnum
import kz.innlab.bookservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList
import kotlin.jvm.Transient

@Entity
@Table(name = "books")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Books: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var name: String? = null

    var author: String? = null

    @Type(type = "string-array")
    @Column(columnDefinition = "character varying[]")
    var content: Array<String> = arrayOf()

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    var category: String? = null

    var subCategory: String? = null

    var avatarId: UUID? = null

    var pdfFileId: UUID? = null
    var fileId: UUID? = null

    var paid: Boolean? = false

    @Enumerated(EnumType.STRING)
    var status: BookStatusEnum? = BookStatusEnum.PUBLIC

    @Transient
    var coauthors: List<Author> = listOf()

    @Transient
    var favorite: Boolean = false
}
