package kz.innlab.userservice.user.model

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*


@Entity(name = "Roles")
@Table
class Role: Auditable<String?>() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    var id: UUID? = null

    var name: String = ""
    var title: String? = null
    var priorityNumber: Long? = null
}
