package kz.innlab.carservice.general.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_wash_order")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Order: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "date_time", columnDefinition = "VARCHAR(255)", nullable = false)
    var dateTime: Timestamp? = null

    @Column(name = "car_wash_box_id", columnDefinition = "uuid", nullable = false, insertable = false, updatable = false)
    var carWashBoxId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY) // or FetchType.EAGER depending on your needs
    var carWashBox: CarWashBox? = null

    @Column(name = "car_wash_price_id", columnDefinition = "uuid", nullable = false, insertable = false, updatable = false)
    var carWashPriceId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY) // or FetchType.EAGER depending on your needs
    var carWashPrice: CarWashPrice? = null

    @Column(name = "car_id", columnDefinition = "uuid", nullable = false, insertable = false, updatable = false)
    var carId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY) // or FetchType.EAGER depending on your needs
    var car: Cars? = null

}
