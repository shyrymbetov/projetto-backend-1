package kz.innlab.bookservice.book.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "BOOK_AUTHORS")
class Author: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "FIRST_NAME")
    var firstName: String? = null

    @Column(name = "LAST_NAME")
    var lastName: String? = null

    @Column(name = "MIDDLE_NAME")
    var middleName: String? = null

//    TODO numberBooks update when book created
    @Transient
    var numberBooks: Int = 0

    @Transient
    var initialName: String? = null
        get() {
            var result = ""
            if (firstName != null && firstName!!.length > 1) {
                result = firstName!![0].toString() + ". "
                if (lastName == null) {
                    result = firstName!!
                } else {
                    result += lastName
                }
            }
            return result
        }

    @Transient
    var fio: String? = null
        get() {
            val result = arrayListOf<String>()
            if (firstName != null) {
                result.add(firstName!!)
            }
            if (lastName != null) {
                result.add(lastName!!)
            }
            if (middleName != null) {
                result.add(middleName!!)
            }
            return result.joinToString(" ")
        }
}
