package kz.innlab.bookservice.book.dto

import java.util.*

class AccountDTO {
     var id: UUID? = null
     var firstName: String? = null
     var lastName: String? = null
     var middleName: String? = null
     var avatar: UUID? = null
     var fio: String? = null
     var fioShort: String? = null
     var cities: Set<CityDTO> = HashSet()
     var cityIds: List<UUID> = listOf()
     override fun toString(): String {
          return "AccountDTO(id=$id, firstName=$firstName, lastName=$lastName, middleName=$middleName, avatar=$avatar, fio=$fio)"
     }
}
