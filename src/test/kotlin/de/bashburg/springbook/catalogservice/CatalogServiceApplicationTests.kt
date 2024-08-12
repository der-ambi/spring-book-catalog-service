package de.bashburg.springbook.catalogservice

import de.bashburg.springbook.catalogservice.domain.Book
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `when post request then book created`() {
        val expectedBook = Book("1231231231231", "Title", "Author", 9.90)

        webTestClient
            .post()
            .uri("/books")
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Book>()
            .consumeWith {
                val responseBody = it.responseBody
                assertNotNull(responseBody)
                assertEquals(expectedBook.isbn, responseBody.isbn)
            }
    }
}
