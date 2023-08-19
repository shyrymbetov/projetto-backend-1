package kz.innlab.bookservice.system.dto

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 28.08.2022
 */
data class TelegramMessageDto(
    var text: String,
    var chatId: String? = null
)
