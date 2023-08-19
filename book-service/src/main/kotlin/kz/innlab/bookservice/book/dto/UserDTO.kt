package kz.innlab.bookservice.book.dto

import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

class UserDTO {
    var id: UUID? = null
    var email: String = ""
    var phone: String = ""
    var roles: List<String> = arrayListOf()
//    var blocked: Timestamp? = null
//    var loginAttempts: Int? = 0
}
