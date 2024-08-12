package de.bashburg.springbook.catalogservice.web

import de.bashburg.springbook.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import kotlin.test.Test

@JsonTest
class BookJsonTests() {

    @Autowired
    lateinit var json: JacksonTester<Book>

    @Test
    fun testSerialize() {
        val book = Book("1234567890", "Title", "Author", 8.80)
        val jsonContent = json.write(book)
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn)
        //TODO add missing assertions
    }

    @Test
    fun testDeserialize() {
        val content = """
            |{
            |    "isbn": "1234567890",
            |    "title": "Title",
            |    "author": "Author",
            |    "price": 8.80
            |}
        """.trimMargin()

        assertThat(json.parse(content)).usingRecursiveComparison()
            .isEqualTo(Book(isbn = "1234567890", title = "Title", author = "Author", price = 8.80))
    }
}