package kz.innlab.bookservice.system.client

import kz.innlab.bookservice.book.dto.AccountDTO
import kz.innlab.bookservice.book.dto.CityDTO
import kz.innlab.bookservice.system.dto.PermissionDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@FeignClient(name = "user-service")
interface UserServiceClient {

    @GetMapping("/users/structure/user-id/{userId}")
    fun getUserStructure(@PathVariable(value = "userId") userId: UUID): Optional<AccountDTO>

    @PostMapping("/users/regions/is-valid-city/{regionId}/{cityId}")
    fun isValidCity(@PathVariable("regionId") regionId: UUID, @PathVariable("cityId") cityId: UUID): Boolean

    @GetMapping("/users/cities/{id}")
    fun getCityById(@PathVariable("id") id: UUID): Optional<CityDTO>

    @GetMapping("/users/cities/list/user-id/{userId}")
    fun getListCitiesByUserId(@PathVariable userId: UUID): List<CityDTO>

    @GetMapping("/users/user/{id}")
    fun getAccountById(@PathVariable("id") id: UUID): Optional<AccountDTO>

    @GetMapping("/users/username/{username}")
    fun getAccountByUsername(@PathVariable(value = "username") username: String): Optional<AccountDTO>

    @PostMapping("/users/list/users")
    fun getUsersIds(@Valid @RequestBody ids: List<UUID>): List<AccountDTO>

    @PostMapping("/users/list/users")
    fun getStudentsIn(studentIds: List<UUID>): List<AccountDTO>

    @PostMapping("/users/cities/list")
    fun getListMyCitiesByUsername(username: String): List<CityDTO>

    @GetMapping("/users/cities/server")
    fun getListCitiesIn(citiesId: List<UUID>): List<CityDTO>

    @PostMapping("/users/permissions/crud")
    fun permission(@RequestBody item: PermissionDTO): MutableMap<String, Int>

    @GetMapping("/users/city/account/list/server/student/{userId}")
    fun getListOptionByCity(@PathVariable userId: String): List<AccountDTO>

    @GetMapping("/users/students/{username}")
    fun getMyStudentList(
        @RequestParam params: MutableMap<String, String>,
        @PathVariable username: String
    ): List<AccountDTO>

}
