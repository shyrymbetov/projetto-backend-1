package kz.innlab.userservice.user.service

import freemarker.template.Configuration
import freemarker.template.Template
import kz.innlab.userservice.system.client.AuthServiceClient
import kz.innlab.userservice.system.dto.EmailChangeDTO
import kz.innlab.userservice.system.dto.MailMessageDto
import kz.innlab.userservice.user.dto.PasswordDTO
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.dto.UserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.security.Principal
import java.util.*

@Service
class UserActionsServiceImpl: UserActionsService {

    @Autowired
    lateinit var authClient: AuthServiceClient

    @Autowired
    lateinit var templateConfiguration: Configuration


    @Autowired
    lateinit var userService: UserService

    override fun changePassword(principal: Principal, passwordDTO: PasswordDTO): Status {
        var status = Status()
        userService.getUserById(UUID.fromString(principal.name)).ifPresentOrElse({
            passwordDTO.userId = it.id
            status = authClient.changePassword(passwordDTO)
        }, {
            status.message = "Can not find User"
        })
        return status
    }

    override fun sendVerifyCodeToEmail(email: String): Status {
        val status = Status()
        val resetPasswordLink = authClient.generateCode(email)
        if (resetPasswordLink.isNullOrBlank()) {
            status.value = "Cannot find user"
        } else {
            val split = resetPasswordLink.split(":")
            if (split.size == 2) {
                val mailMessage = MailMessageDto()
                mailMessage.to = email
                mailMessage.subject = "Password Reset"

                val model = mapOf(
                    "code1" to split[1][0],
                    "code2" to split[1][1],
                    "code3" to split[1][2],
                    "code4" to split[1][3],
                )
                val template: Template = templateConfiguration.getTemplate("reset-password.ftl")

                mailMessage.content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
                status.status = 1
                status.message = "OK"
                status.value = split.first()
            } else {
                return status
            }
        }
        return status
    }

    override fun changePasswordByToken(passwordDto: PasswordDTO): Status {
        return authClient.changePasswordByToken(passwordDto)
    }

    override fun verifyUserEmail(email: EmailChangeDTO): Status {
        return authClient.verifyEmail(email)
    }

    override fun changeUserEmail(emailDto: EmailChangeDTO): Status {
        return authClient.verifyEmail(emailDto)
    }

    override fun hasRole(roles: List<String>, username: String): Status {
        val status = Status()
        try {
            val user = userService.getUserByIdForService(UUID.fromString(username))
            if (user.isPresent) {
                if (roles.isEmpty()) {
                    status.status = 1
                    return status
                }
                for (role in roles) {
                    if (user.get().rolesCollection.stream()
                            .anyMatch { it.name.uppercase().trim() == role.uppercase().trim() }) {
                        status.status = 1
                        return status
                    }
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return status
    }

    override fun checkEmail(newUserRequest: UserRequest): Status {
        return userService.checkEmail(newUserRequest)
    }

    override fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status {
        return userService.changeStatusBlocked(statusFilter)
    }
}
