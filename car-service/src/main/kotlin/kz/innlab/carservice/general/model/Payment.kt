package kz.innlab.carservice.general.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size


@Entity
@Table(name = "payments")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Payment: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "OWNER", columnDefinition = "character varying")
    var owner: String? = null

    @Column(name = "CARD_NUMBER", columnDefinition = "character varying")
    var cardNumber: String? = null

    @Column(name = "CVV", columnDefinition = "character varying")
    var cvv: String? = null

    @Column(name = "EXPIRES_DATE", columnDefinition = "character varying")
    var expiresDate: String? = null

    @Column(name = "USER_ID")
    var userId: UUID? = null

}
