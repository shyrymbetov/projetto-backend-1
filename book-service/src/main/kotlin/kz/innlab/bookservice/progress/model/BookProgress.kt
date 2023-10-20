package kz.innlab.bookservice.progress.model

import com.fasterxml.jackson.annotation.JsonIgnore
import kz.innlab.bookservice.system.model.Auditable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "book_progress"
)
class BookProgress: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var startTime: Timestamp? = null

    var endTime: Timestamp? = null

    @ManyToOne
    @JoinColumn(name = "book_statistics_id") // Define the join column name
    @JsonIgnore(value = true)
    var bookStatistics: BookStatistics? = null
}
