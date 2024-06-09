package kz.innlab.mainservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "resources")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Resource {
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

    @Column(name = "TYPE")
    var type: String? = null

    @Type(type = "uuid-array")
    @Column(name = "liked_users", columnDefinition="uuid[]")
    @JsonIgnore
    var likedUsers: Array<UUID> = arrayOf()

    val likes: Int
        get() {
            return likedUsers.size
        }

    var isFavorite: Boolean = false

}
