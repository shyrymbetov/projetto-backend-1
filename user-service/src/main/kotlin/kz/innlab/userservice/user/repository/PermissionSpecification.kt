package kz.innlab.userservice.user.repository

import kz.innlab.userservice.user.model.Permission
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
class PermissionSpecification {
    companion object {
        fun deletedAtIsNull(): Specification<Permission> {
            return Specification<Permission> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }

        fun moduleEqual(value: String?): Specification<Permission> {
            return Specification<Permission> { root, query, builder ->
                if (!value.isNullOrBlank()) {
                    builder.equal(root.get<String>("module"), value.uppercase())
                } else {
                    null
                }
            }
        }

        fun chapterEqual(value: String?): Specification<Permission> {
            return Specification<Permission> { root, query, builder ->
                if (!value.isNullOrBlank()) {
                    builder.equal(root.get<String>("chapter"), value.trim())
                } else {
                    null
                }
            }
        }

        private fun concat(
            delimiter: String,
            criteriaBuilder: CriteriaBuilder,
            vararg expressions: Expression<String>
        ): Expression<String>? {
            var result: Expression<String>? = null
            for (i in expressions.indices) {
                val first = i == 0
                val last = i == (expressions.size - 1)
                val expression: Expression<String> = expressions[i]
                if (first && last) {
                    result = expression
                } else if (first) {
                    result = criteriaBuilder.concat(expression, delimiter)
                } else {
                    result = criteriaBuilder.concat(result, expression)
                    if (!last) {
                        result = criteriaBuilder.concat(result, delimiter)
                    }
                }
            }
            return result
        }
    }
}
