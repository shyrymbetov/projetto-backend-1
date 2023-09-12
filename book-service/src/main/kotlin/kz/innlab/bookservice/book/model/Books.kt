package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "books")
class Books: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var name: String? = null

    var author: String? = null

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    var category: String? = null

    var subCategory: String? = null

    var avatarId: UUID? = null

    @Transient
    var coauthors: List<Author> = listOf()
}
