package kz.innlab.carservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "cars")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class Cars: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "VRP", columnDefinition = "character varying", nullable = false, unique = true)
    var vrp: String? = null

    @Column(name = "MODEL", columnDefinition = "character varying", nullable = false)
    var model: String? = null

    @Column(name = "MARK", columnDefinition = "character varying", nullable = false)
    var mark: String? = null

    var color: String? = null

    @Column(name = "OWNER", nullable = false)
    var owner: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_body_id") // Specify the foreign key column name
//    @JsonIgnore()
    var carBody: CarBody? = null

    @Column(name = "car_body_id", insertable = false, updatable = false, nullable = false)
    var carBodyId: UUID? = null

}
