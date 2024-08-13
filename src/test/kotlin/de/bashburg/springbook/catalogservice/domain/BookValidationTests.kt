package de.bashburg.springbook.catalogservice.domain

import jakarta.validation.Validation
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BookValidationTests {
    companion object {
        val validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun whenAllFieldsCorrectThenValidationSucceeds() {
        val book = Book.of("1234567890", "Test Title", "Test Author", 9.90)
        Assertions.assertThat(validator.validate(book)).isEmpty()
    }

    @Test
    fun whenIsbnIsDefinedButIncorrectThenValidationFails() {
        val book = Book.of("a234567890", "Test Title", "Test Author", 9.90)
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message).isEqualTo("The ISBN format must be valid.")
    }
}