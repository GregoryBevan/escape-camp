package me.elgregos.escapecamp.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebInputException

private val logger = KotlinLogging.logger {}

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: GlobalErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) :
    AbstractErrorWebExceptionHandler(errorAttributes, webProperties.resources, applicationContext) {

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?) =
        RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)

    private fun renderErrorResponse(request: ServerRequest) =
        getErrorAttributes(
            request,
            ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        )
            .let {
                val status = it["status"].let { s -> if (s is Int) s else 500 }
                ServerResponse
                    .status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(it))
            }
}

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(
        request: ServerRequest,
        options: ErrorAttributeOptions
    ): Map<String, Any> = when (val error = getAndLogError(request)) {
        is GameException -> {
            val errorAttributes: MutableMap<String, Any> = mutableMapOf()
            errorAttributes["path"] = request.path()
            errorAttributes["status"] = error.status.value()
            errorAttributes["error"] = error.status.reasonPhrase
            errorAttributes["message"] = error.message
            errorAttributes["exception"] = error.javaClass.simpleName
            errorAttributes
        }

        is WebExchangeBindException -> {
            val errorAttributes: MutableMap<String, Any> = mutableMapOf()
            errorAttributes["path"] = request.path()
            errorAttributes["status"] = HttpStatus.BAD_REQUEST.value()
            errorAttributes["error"] = HttpStatus.BAD_REQUEST.reasonPhrase
            errorAttributes["message"] = "Validation error(s) occurred : ${error.errorCount}"
            errorAttributes["errors"] = error.allErrors.map {
                mapOf(
                    "field" to it.codes?.get(1)?.replace("${it.code}.", ""),
                    "message" to it.defaultMessage
                )
            }
            errorAttributes
        }

        is ServerWebInputException -> {
            val errorAttributes: MutableMap<String, Any> = mutableMapOf()
            errorAttributes["path"] = request.path()
            errorAttributes["status"] = HttpStatus.INTERNAL_SERVER_ERROR.value()
            errorAttributes["error"] = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            errorAttributes["message"] = "Invalid json for request payload"
            errorAttributes["errors"] = error.message
            errorAttributes
        }

        is BadCredentialsException ->
             mutableMapOf<String, Any>().also {
                 it["status"] = HttpStatus.FORBIDDEN.value()
                 it["error"] = HttpStatus.FORBIDDEN.reasonPhrase
                 it["message"] = error.message ?: "Wrong username or password"

        }

        else -> super.getErrorAttributes(request, options)
    }

    fun getAndLogError(request: ServerRequest?): Throwable {
        val error = super.getError(request)
        logger.error(error) { "An error occurred while calling Escape Camp Api" }
        return error
    }
}
