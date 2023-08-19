package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.*
import kz.innlab.bookservice.book.model.payload.BookLevel
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp
import java.util.*

class BookSpecification {

    companion object {
        fun containsName(name: String): Specification<Book> {
            return Specification<Book> { root, query, builder ->
                if (name.isNotBlank()) {
                    builder.like(builder.lower(root.get("name")), "%${name.lowercase()}%")
                } else {
                    null
                }
            }
        }

        fun equalsLevel(level: BookLevel?): Specification<Book> {
            return Specification<Book> { root, query, builder ->
                if (level != null) {
                    builder.equal(root.get<BookLevel>("level"), level)
                } else {
                    null
                }
            }
        }

        fun containsGenreId(genreId: UUID?): Specification<Book> {
            return Specification<Book> { root, query, builder ->
                if (genreId != null) {
                    builder.isNotNull(builder.function("array_position",
                        Int::class.java, root.get<Array<UUID>>("genreIds"), builder.literal(genreId)))
                } else {
                    null
                }
            }
        }

        fun containsAuthorId(authorId: UUID?): Specification<Book> {
            return Specification<Book> { root, query, builder ->
                if (authorId != null) {
                    builder.isNotNull(builder.function("array_position",
                        Int::class.java, root.get<Array<UUID>>("authorIds"), builder.literal(authorId)))
                } else {
                    null
                }
            }
        }

        fun deletedAtIsNull(): Specification<Book> {
            return Specification<Book> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }

    }
}
