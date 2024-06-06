package kz.innlab.mainservice.system.exceptionHandler

import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 17.04.2022
 */

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @Value("\${reflectoring.trace: false}")
    private val printStackTrace = false

    @Value("\${spring.application.name: service}")
    private val applicationName = ""

    private var log = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Validation error. Check 'errors' field for details."
        )
        for (fieldError in ex.bindingResult.fieldErrors) {
            errorResponse.addValidationError(fieldError.field, fieldError.defaultMessage)
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse)
    }

    @ExceptionHandler(NoSuchElementFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoSuchElementFoundException(
        itemNotFoundException: NoSuchElementFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error("Failed to find the requested element", itemNotFoundException)
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request)
    }

    private fun buildErrorResponse(
        exception: Exception,
        httpStatus: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(exception, exception.message, httpStatus, request)
    }

    private fun buildErrorResponse(
        exception: Exception,
        message: String?,
        httpStatus: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(httpStatus.value(), message)
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.stackTrace = ExceptionUtils.getStackTrace(exception)
        }
        return ResponseEntity.status(httpStatus).body(errorResponse)
    }

    private fun isTraceOn(request: WebRequest): Boolean {
        val value = request.getParameterValues(TRACE)
        return Objects.nonNull(value) && !value.isNullOrEmpty() && value.first()!!.contentEquals("true")
    }

    public override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(ex, status, request)
    }

    companion object {
        const val TRACE = "trace"
    }
}
