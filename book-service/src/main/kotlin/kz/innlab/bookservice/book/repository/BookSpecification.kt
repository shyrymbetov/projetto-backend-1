package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.*
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp
import java.util.*

class BookSpecification {

    companion object {
        fun containsName(name: String): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                if (name.isNotBlank()) {
                    builder.like(builder.lower(root.get("name")), "%${name.lowercase()}%")
                } else {
                    null
                }
            }
        }

        fun author(name: UUID?): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                if (name != null) {
                    builder.equal(root.get<String>("creator"), name.toString())
                } else {
                    null
                }
            }
        }

        fun bookIdIn(bookIds: List<UUID>): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                builder.and(root.get<UUID>("id").`in`(bookIds))
            }
        }

        fun deletedAtIsNull(): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }

    }
}
