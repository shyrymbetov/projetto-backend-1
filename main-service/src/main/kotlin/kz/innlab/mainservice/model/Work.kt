package kz.innlab.mainservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.mainservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "works")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Work: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "TITLE_EN", columnDefinition = "character varying")
    var titleEn: String? = null

    @Column(name = "TITLE_RU", columnDefinition = "character varying")
    var titleRu: String? = null

    @Column(name = "IMAGE")
    var image: UUID? = null

    @Column(name = "FILE")
    var file: UUID? = null

    @Column(name = "AUTHOR")
    var author: UUID? = null

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

    var isFavorite: Boolean = false
}
