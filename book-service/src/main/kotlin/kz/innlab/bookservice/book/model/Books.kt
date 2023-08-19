package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "books")
class Books: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var name: String? = null

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    @Column(name = "avatar_id")
    var avatarId: UUID? = null


}
