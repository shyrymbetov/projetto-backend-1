package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.*
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp

class AuthorSpecification {
    companion object {
        fun containsFirstName(firstName: String): Specification<Author> {
            return Specification<Author> { root, query, builder ->
                if (firstName.isNotBlank()) {
                    builder.like(builder.lower(root.get("firstName")), "%${firstName.lowercase()}%")
                } else {
                    null
                }
            }
        }
        fun containsLastName(lastName: String): Specification<Author> {
            return Specification<Author> { root, query, builder ->
                if (lastName.isNotBlank()) {
                    builder.like(builder.lower(root.get("lastName")), "%${lastName.lowercase()}%")
                } else {
                    null
                }
            }
        }

        fun deletedAtIsNull(): Specification<Author> {
            return Specification<Author> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }
    }
}
