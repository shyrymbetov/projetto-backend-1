package kz.innlab.carservice.general.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

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

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "start_time", columnDefinition = "TIME", nullable = false)
    var startTime: Time? = null

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "end_time", columnDefinition = "TIME", nullable = false)
    var endTime: Time? = null

    @Column(name = "PHONE", unique = true, nullable = false)
    var phone: String = ""
        set(value) {
            field = value.lowercase().replace(" ", "") // remove all space
        }

    @Column(name = "EMPLOYEE", nullable = false)
    var employee: UUID? = null

    @Type(type = "uuid-array")
    @Column(name = "PHOTO", columnDefinition="uuid[]")
    var headings: Array<UUID> = arrayOf()

    @Column(name = "DESCRIPTION", columnDefinition = "character varying", nullable = true)
    var description: String? = null

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carWashBoxes: MutableList<CarWashBox> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carWashPrice: MutableList<CarWashPrice> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carWashWorker: MutableList<CarWashWorker> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carFixBoxes: MutableList<CarFixBox> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val carFixes: MutableList<CarFix> = mutableListOf()

    @OneToMany(mappedBy = "washingCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val review: MutableList<UserWashingCenterReview> = mutableListOf()

    val averageRating: Float
        get() {
            var rating = 0.0
            var count = 0
            review.map {
                rating += it.rating!!
                count += 1
            }

            return rating.toFloat() / count
        }
}


