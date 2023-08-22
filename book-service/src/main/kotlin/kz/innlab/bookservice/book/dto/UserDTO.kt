package kz.innlab.bookservice.book.dto

import java.util.*

class UserDTO {
    var id: UUID? = null
    var email: String = ""
    var roles: List<String> = arrayListOf()
}
