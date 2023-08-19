package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.RegistrationUserDto
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.dto.UserRequest
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun getUserById(id: UUID): Optional<User>
    fun getUserListByIds(ids: List<UUID>): ArrayList<User>
    fun getUserListByIdsArchive(ids: List<UUID>): ArrayList<User>
    fun getUserListByRoles(roles:  List<String>): ArrayList<User>

    fun createNewUser(user: UserRequest): Status
    fun saveChanges(user: UserRequest): Status

    //for User delete
    fun moveToTrash(id: UUID): Status
    fun delete(id: UUID): Status
    fun restore(id: UUID): Status

    //Current User
    fun getCurrentUser(name: String): Optional<User>
    fun registration(user: RegistrationUserDto): Status
    fun saveChangesCurrentUser(user: UserRequest, username: String): Status
    fun deleteCurrentUser(name: String): Status

    //for User Actions
    fun checkEmail(newUserRequest: UserRequest): Status
    fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status

}
