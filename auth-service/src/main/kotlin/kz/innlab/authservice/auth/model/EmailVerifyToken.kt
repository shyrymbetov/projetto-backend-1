package kz.innlab.authservice.auth.model

import java.util.Date
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
class EmailVerifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "TOKEN", columnDefinition = "character varying")
    var token: String? = null

    @NotNull
    @Column(columnDefinition = "character varying")
    var code: String? = null

    var oldEmail: Boolean = false
    var newEmail: Boolean = false

    var verified: Boolean = false

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    var user: User? = null

    var expiryDate: Date? = null

    companion object {
        const val EXPIRATION = 5 // 5 min
    }
}
