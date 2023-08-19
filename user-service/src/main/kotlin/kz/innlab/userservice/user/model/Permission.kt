package kz.innlab.userservice.user.model

import java.util.*
import javax.persistence.*
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
@Entity(name = "PERMISSIONS")
@Table(uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("roleId", "module", "chapter", "deletedAt"))])
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    ),
    TypeDef(
        name = "uuid-array",
        typeClass = UUIDArrayType::class
    ),
    TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType::class
    )
)
class Permission : Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var roleId: UUID? = null

    @NotBlank
    @Column(columnDefinition = "character varying")
    var module: String = ""
        set(value) {
            field = value.trim().uppercase()
        }

    @Column(columnDefinition = "character varying")
    var chapter: String? = null

    @Range(min = 0, max = 1)
    @Column(name = "create_permission", nullable = false)
    var create: Int = 0

    @Range(min = 0, max = 1)
    @Column(name = "read_permission", nullable = false)
    var read: Int = 0

    @Range(min = 0, max = 1)
    @Column(name = "update_permission", nullable = false)
    var update: Int = 0

    @Range(min = 0, max = 1)
    @Column(name = "delete_permission", nullable = false)
    var delete: Int = 0

    @ManyToOne
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    var role: Role? = null

    override fun toString(): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(this)
    }
}
