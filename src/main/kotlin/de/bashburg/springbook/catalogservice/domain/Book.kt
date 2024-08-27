package de.bashburg.springbook.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.springframework.data.annotation.*
import java.time.Instant

data class Book(
    @Id
    val id: Long? = null,

    @Version
    val version: Int = 0,

    @CreatedDate
    val createdDate: Instant?,

    @CreatedBy
    val createdBy: String?,

    @LastModifiedDate
    val lastModifiedDate: Instant?,

    @LastModifiedBy
    val lastModifiedBy: String?,

    @field:NotBlank(message = "The book ISBN must be defined.")
    @field:Pattern(
        regexp = "^([0-9]{10}|[0-9]{13})$",
        message = "The ISBN format must be valid."
    ) val isbn: String,

    @field:NotBlank(message = "The book title must be defined.")
    val title: String,

    @field:NotBlank(message = "The book author must be defined.")
    val author: String,

    val publisher: String?,

    @field:NotNull(message = "The book price must be defined.")
    @field:Positive(message = "The book price must be greater than zero.")
    val price: Double
) {
    companion object {
        fun of(isbn: String, title: String, author: String, publisher: String?, price: Double): Book =
            Book(
                id = null,
                version = 0,
                createdDate = null,
                createdBy = null,
                lastModifiedDate = null,
                lastModifiedBy = null,
                isbn = isbn,
                title = title,
                author = author,
                publisher = publisher,
                price = price
            )
    }
}
