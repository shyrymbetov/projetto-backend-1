package kz.innlab.fileservice.dto

/**
 * @project microservice-template
 * @author Yerassyl Shyrymbetov on 28.08.2022
 */
data class TelegramMessageDto(
    var text: String,
    var chatId: String? = null
)
