package kz.innlab.carservice.general.model

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

    @Column()
    var carBodyTypeId: UUID? = null

    @ManyToOne
    var washingCenter: WashingCenter? = null

}
