package kz.innlab.userservice.user.repository

import kz.innlab.userservice.user.model.Role
import kz.innlab.userservice.user.model.User
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Join

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
class UserSpecification {
    companion object {
        fun deletedAtIsNull(): Specification<User> {
            return Specification<User> { root, query, builder ->
                builder.isNull(root.get<Timestamp>("deletedAt"))
            }
        }

        fun fioLike(value: String): Specification<User> {
            return Specification<User> { root, query, builder ->
                builder.like(builder.lower(concat(" ", builder, root.get("firstName"), root.get("lastName"))), "%${value.lowercase()}%")
            }
        }

        fun containRoles(value: String): Specification<User> {
            return Specification<User> { root, query, builder ->
                val join: Join<User, Role> = root.join("rolesCollection")
                builder.equal(join.get<String>("name"), value)
            }
        }

        private fun concat(delimiter: String, criteriaBuilder: CriteriaBuilder, vararg expressions: Expression<String>): Expression<String>? {
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
