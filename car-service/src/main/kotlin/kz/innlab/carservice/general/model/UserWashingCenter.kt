package kz.innlab.carservice.general.model

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*


@Entity
@Table(
    name = "user_washing_center",
    uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("user_id", "washing_center_id"))]
)
class UserWashingCenter {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    var id: UUID? = null

    @Column(name = "USER_ID", nullable = false)
    var userId: UUID? = null

    @Column(name = "WASHING_CENTER_ID", nullable = false)
    var washingCenterId: UUID? = null

    // Additional properties specific to this relationship can be added here
}