package de.bashburg.springbook.catalogservice.web

import de.bashburg.springbook.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import kotlin.test.Test

@JsonTest
class BookJsonTests {

    @Autowired
    lateinit var json: JacksonTester<Book>

    @Test
    fun testSerialize() {
        val book = Book.of("1234567890", "Title", "Author", "Polarsophia", 8.80)
        val jsonContent = json.write(book)
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn)
        //TODO add missing assertions
    }

    @Test
    fun testDeserializeWithoutPublisher() {
        val content = """
            |{
            |    "isbn": "1234567890",
            |    "title": "Title",
            |    "author": "Author",
            |    "price": 8.80
            |}
        """.trimMargin()

        assertThat(json.parse(content)).usingRecursiveComparison()
            .isEqualTo(Book.of(isbn = "1234567890", title = "Title", author = "Author", publisher = null, price = 8.80))
    }

    fun testDeserializeWithPublisher() {
        val content = """
            |{
            |    "isbn": "1234567890",
            |    "title": "Title",
            |    "author": "Author",
            |    "publisher": "Polarsophia",
            |    "price": 8.80
            |}
        """.trimMargin()

        assertThat(json.parse(content)).usingRecursiveComparison()
            .isEqualTo(
                Book.of(
                    isbn = "1234567890",
                    title = "Title",
                    author = "Author",
                    publisher = "Polarsophia",
                    price = 8.80
                )
            )
    }
}