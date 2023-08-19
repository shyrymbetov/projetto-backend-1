package kz.innlab.authservice.auth.model

import kz.innlab.authservice.system.model.Auditable
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

    @Column(name = "NAME", columnDefinition = "character varying")
    var name: String = ""
        get() = field.uppercase()
        set(value) {
            field = value.uppercase()
        }

    @Column(name = "TITLE", columnDefinition = "character varying")
    var title: String? = null
        set(value) {
            field = value
            if (value != null) {
                field = value.trim()
            }
        }

    @Column(name = "PRIORITY_NUMBER")
    var priorityNumber: Long? = null
}
