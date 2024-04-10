package kz.innlab.carservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladmihalcea.hibernate.type.array.StringArrayType
import kz.innlab.carservice.system.model.Auditable
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "car_body")
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
class CarBody: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "type", columnDefinition = "character varying", nullable = false, unique = true)
    var type: String? = null

    @Column(name = "ru_name", columnDefinition = "character varying")
    var ruName: String? = null

    @OneToMany(mappedBy = "carBody", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    val carWashPrice: MutableList<CarWashPrice> = mutableListOf()
}
