package kz.innlab.mainservice.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*


@Entity
@Table(
    name = "user_washing_center_review",
    uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("user_id", "washing_center_id"))]
)
class UserWashingCenterReview {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    var id: UUID? = null

    @Column(name = "USER_ID", nullable = false)
    var userId: UUID? = null

    @Column(name = "RATING", nullable = false)
    var rating: Int? = null

    @Column(name = "REVIEW")
    var review: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "washing_center_id") // Specify the foreign key column
    @JsonIgnore()
    var washingCenter: WashingCenter? = null

    @Column(name = "washing_center_id", insertable = false, updatable = false, nullable = false)
    var washingCenterId: UUID? = null

    // Additional properties specific to this relationship can be added here
}