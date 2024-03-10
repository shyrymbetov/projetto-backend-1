package kz.innlab.carservice.general.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_washing_center")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class WashingCenter: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "NAME", columnDefinition = "character varying", nullable = false)
    var name: String? = null

    @Column(name = "LOCATION", columnDefinition = "character varying", nullable = false)
    var location: String? = null

    @Column(name = "LON", nullable = false)
    var lon: String? = null

    @Column(name = "LAT", nullable = false)
    var lat: String? = null

    @Column(name = "PHONE", unique = true, nullable = false)
    var phone: String = ""
        set(value) {
            field = value.lowercase().replace(" ", "") // remove all space
        }

    @Column(name = "EMPLOYEE", nullable = false)
    var employee: UUID? = null

    @Column(name = "DESCRIPTION", columnDefinition = "character varying", nullable = true)
    var description: String? = null

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carWashBoxes: MutableList<CarWashBox> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carWashPrice: MutableList<CarWashPrice> = mutableListOf()

}


