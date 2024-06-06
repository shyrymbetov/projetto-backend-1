package kz.innlab.mainservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.mainservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "car_wash_workers")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class CarWashWorker: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "FIRST_NAME", columnDefinition = "character varying")
    var firstName: String? = null

    @Column(name = "LAST_NAME", columnDefinition = "character varying")
    var lastName: String? = null

    @Column(name = "AVATAR")
    var avatar: UUID? = null

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


    @Column(name = "PHONE", unique = true, nullable = false)
    var phone: String = ""
        set(value) {
            field = value.lowercase().replace(" ", "") // remove all space
        }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "washing_center_id") // Specify the foreign key column
    @JsonIgnore()
    var washingCenter: WashingCenter? = null

    @Column(name = "washing_center_id", insertable = false, updatable = false, nullable = false)
    var washingCenterId: UUID? = null
}
