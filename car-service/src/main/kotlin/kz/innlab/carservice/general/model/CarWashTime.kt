package kz.innlab.carservice.general.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_wash_time")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class CarWashTime: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column()
    var cost: Int? = null

    @Column()
    var carBodyTypeId: UUID? = null

    @ManyToOne
    @JoinColumn(name = "car_wash_box_id") // Specify the foreign key column
    var carWashBox: CarWashBox? = null
}
