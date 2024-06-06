package kz.innlab.mainservice.system.exceptionHandler

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 17.04.2022
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorResponse(val status: Int, val message: String?) {

    var stackTrace: String? = null
    var errors: MutableList<ValidationError> = mutableListOf()
        private set

    class ValidationError(
        val field: String? = null,
        val message: String? = null
    ) { }

    fun addValidationError(field: String?, message: String?) {
        if (Objects.isNull(errors)) {
            errors = ArrayList()
        }
        errors.add(ValidationError(field, message))
    }
}

