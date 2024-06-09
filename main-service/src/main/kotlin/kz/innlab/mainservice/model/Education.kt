package kz.innlab.mainservice.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "education_link")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Education {
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

    @Column(name = "LINK", columnDefinition = "character varying")
    var link: String? = null

    @Column(name = "ICON")
    var icon: UUID? = null

    @Column(name = "IMAGE")
    var image: UUID? = null

}
