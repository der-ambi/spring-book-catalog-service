package de.bashburg.springbook.catalogservice.web

import de.bashburg.springbook.catalogservice.domain.BookAlreadyExistsException
import de.bashburg.springbook.catalogservice.domain.BookNotFoundException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

// FIXME add proper RFC 9457, ResponseEntityExceptionHandler does not do very good job
@RestControllerAdvice
class BookControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BookNotFoundException::class)
    fun bookNotFoundHandler(ex: BookNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.message)

    @ExceptionHandler(BookAlreadyExistsException::class)
    fun bookAlreadyExistsHandler(ex: BookAlreadyExistsException): ProblemDetail = ProblemDetail.forStatusAndDetail(
        UNPROCESSABLE_ENTITY, ex.message
    )
}