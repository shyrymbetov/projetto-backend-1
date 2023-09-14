package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.dto.BookStatusEnum
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

        fun bookStatusPublic(): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                builder.equal(root.get<BookStatusEnum>("status"), BookStatusEnum.PUBLIC)
            }
        }

        fun categoryEquals(category: String): Specification<Books> {
            return Specification<Books> { root, query, builder ->
                if (category.isNotBlank()) {
                    builder.equal(builder.lower(root.get("category")), category.lowercase())
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
                if (bookIds.isEmpty()) {
                    null
                } else {
                    builder.and(root.get<UUID>("id").`in`(bookIds))
                }
            }
        }

        fun bookIdInNotEmpty(bookIds: List<UUID>): Specification<Books> {
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
