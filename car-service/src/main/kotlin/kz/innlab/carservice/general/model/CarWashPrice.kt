package kz.innlab.carservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_wash_price")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class CarWashPrice: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column()
    var cost: Int? = null

    @Column(name = "car_body_type_id", insertable = false, updatable = false, nullable = false)
    var carBodyId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_body_type_id") // Specify the foreign key column
//    @JsonIgnore()
    var carBody: CarBody? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "washing_center_id") // Specify the foreign key column
    @JsonIgnore()
    var washingCenter: WashingCenter? = null

    @Column(name = "washing_center_id", insertable = false, updatable = false, nullable = false)
    var washingCenterId: UUID? = null

}
