package de.bashburg.springbook.catalogservice

import de.bashburg.springbook.catalogservice.config.DataConfig
import de.bashburg.springbook.catalogservice.domain.Book
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `when post request then book created`() {
        val expectedBook = Book.of("1231231231231", "Title", "Author", "Polarsophia", 9.90)

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
