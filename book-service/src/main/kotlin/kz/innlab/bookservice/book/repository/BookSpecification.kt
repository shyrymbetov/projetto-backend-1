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

        fun author(name: String): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                if (name.isNotBlank()) {
                    builder.equal(root.get<UUID>("creator"), "%${name.lowercase()}%")
                } else {
                    null
                }
            }
        }

        fun containsAuthorId(authorId: UUID?): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                if (authorId != null) {
                    builder.isNotNull(builder.function("array_position",
                        Int::class.java, root.get<Array<UUID>>("authorIds"), builder.literal(authorId)))
                } else {
                    null
                }
            }
        }

        fun deletedAtIsNull(): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }

    }
}
