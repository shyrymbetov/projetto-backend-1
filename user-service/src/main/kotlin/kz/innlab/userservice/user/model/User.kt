package kz.innlab.userservice.user.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.*
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table
import kotlin.jvm.Transient


@Entity(name = "USERS")
@Table
@JsonIgnoreProperties(value = ["password", "rolesCollection"], allowGetters = false, allowSetters = true)
class User: Auditable<String?>() {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    var id: UUID? = null

    @Column(name = "FIRST_NAME", columnDefinition = "character varying")
    var firstName: String? = null

    var name: String? = null

    @Column(name = "LAST_NAME", columnDefinition = "character varying")
    var lastName: String? = null

    @Column(name = "AVATAR")
    var avatar: UUID? = null

    @Enumerated(EnumType.STRING)
    var provider: UserProviderType? = null

    @Transient
    var fio: String? = null
        get() {
            val result: ArrayList<String> = arrayListOf()
            if (!firstName.isNullOrBlank()) {
                result.add(firstName!!)
            }
            if (!lastName.isNullOrBlank()) {
                result.add(lastName!!)
            }
            return result.joinToString(" ").trim()
        }

    @Transient
    var fioShort: String? = null
        get() {
            val result: StringBuilder = StringBuilder("")
            if (!lastName.isNullOrBlank()) {
                result.append("${lastName!!.first()}.")
            }
            return "$firstName ${result.trim()}"
        }


    @Column(name = "EMAIL", unique = true, nullable = false)
    var email: String = ""
        set(value) {
            field = value.lowercase().trim()
        }

    @Column(name = "PASSWORD", columnDefinition = "character varying", nullable = false)
    var password: String = ""

    var enabled: Boolean? = null
    var blocked: Timestamp? = null
    var blockCodeSend: Timestamp? = null
    var receivingFailedCountCode: Int? = 0

    var loginAttempts: Int? = 0

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinTable(name = "USERS_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")])
    var rolesCollection: Collection<Role> = listOf()

    @Transient
    var roles: List<String> = arrayListOf()
        get() {
            field = arrayListOf()
            for (item in rolesCollection) {
                field = field.plus(item.name)
            }
            return field
        }

    fun incrementLoginAttempts() { loginAttempts = loginAttempts!! + 1 }

}
