package kz.innlab.authservice.auth.dto

import java.sql.Timestamp

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 04.07.2022
 */
class EventLogs {
    var title: String? = null
    var description: String? = null
    var subjectId: String? = null
    var objectId: String? = null
    var date: Timestamp = Timestamp(System.currentTimeMillis())
        private set
}
