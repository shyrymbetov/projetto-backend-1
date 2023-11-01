package kz.innlab.bookservice.system.dto

import java.sql.Timestamp

/**
 * @project book-service
 * @author Bakytov Nurzhan 11.07.2022
 */
class EventLogs {
    var title: String? = null
    var description: String? = null
    var subjectId: String? = null
    var objectId: String? = null
    var date: Timestamp = Timestamp(System.currentTimeMillis())
        private set
}
