package kz.innlab.mainservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.mainservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "news")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class News: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "TITLE_EN", columnDefinition = "character varying")
    var titleEn: String? = null

    @Column(name = "TITLE_RU", columnDefinition = "character varying")
    var titleRu: String? = null

    @Column(name = "CONTENT_EN", columnDefinition = "character varying")
    var contentEn: String? = null

    @Column(name = "CONTENT_RU", columnDefinition = "character varying")
    var contentRu: String? = null

    @Column(name = "IMAGE")
    var image: UUID? = null

    @Type(type = "uuid-array")
    @Column(name = "liked_users", columnDefinition="uuid[]")
    @JsonIgnore
    var likedUsers: Array<UUID> = arrayOf()

    val isNew: Boolean
        get() {
            return createdAt.after(Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000))
        }

    val likes: Int
        get() {
            return likedUsers.size
        }

    @Transient
    var isFavorite: Boolean = false
}
