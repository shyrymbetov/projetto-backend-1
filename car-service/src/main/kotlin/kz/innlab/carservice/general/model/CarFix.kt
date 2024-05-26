package kz.innlab.carservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_fix")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class CarFix: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "type", columnDefinition = "character varying", nullable = false, unique = true)
    var type: String? = null

    @Column(name = "ru_name", columnDefinition = "character varying")
    var ruName: String? = null

    @Column()
    var cost: Int? = null

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "car_fix_box")
//    @JsonIgnore()
//    var carFixBox: CarFixBox? = null
//
//    @Column(name = "car_fix_box", insertable = false, updatable = false, nullable = false)
//    var carFixBoxId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "washing_center_id") // Specify the foreign key column
    @JsonIgnore()
    var washingCenter: WashingCenter? = null

    @Column(name = "washing_center_id", insertable = false, updatable = false, nullable = false)
    var washingCenterId: UUID? = null
}
