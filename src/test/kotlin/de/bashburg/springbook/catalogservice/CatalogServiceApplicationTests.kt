package de.bashburg.springbook.catalogservice

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import dasniko.testcontainers.keycloak.KeycloakContainer
import de.bashburg.springbook.catalogservice.domain.Book
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CatalogServiceApplicationTests {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `when post request as authenticated user with role employee then book created`() {
        val expectedBook = Book.of("1231231231231", "Title", "Author", "Polarsophia", 9.90)

        webTestClient
            .post()
            .uri("/books")
            .headers { it.setBearerAuth(isabelleToken.accessToken) }
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

    @Test
    fun `when post request as authenticated user with role customer then 401`() {
        val expectedBook = Book.of("1231231231231", "Title", "Author", "Polarsophia", 9.90)

        webTestClient
            .post()
            .uri("/books")
            .headers { it.setBearerAuth(bjornToken.accessToken) }
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `when post request is unauthorized then 403`() {
        val expectedBook = Book.of("1231231231231", "Title", "Author", "Polarsophia", 9.90)

        webTestClient
            .post()
            .uri("/books")
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isUnauthorized
    }

    companion object {

        class KeycloakToken @JsonCreator constructor(@JsonProperty("access_token") val accessToken: String)

        private lateinit var bjornToken: KeycloakToken
        private lateinit var isabelleToken: KeycloakToken

        @Container
        private val keycloakContainer =
            KeycloakContainer("quay.io/keycloak/keycloak:25.0").withRealmImportFile("test-realm-config.json")

        @JvmStatic
        @DynamicPropertySource
        fun staticProperties(dynamicPropertyRegistry: DynamicPropertyRegistry) =
            dynamicPropertyRegistry.add(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                { "${keycloakContainer.authServerUrl}/realms/PolarBookshop" })

        @JvmStatic
        @BeforeAll
        fun generateAccessTokens(): Unit {
            val webClient = WebClient.builder()
                .baseUrl("${keycloakContainer.authServerUrl}/realms/PolarBookshop/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()

            this.isabelleToken = authenticateWith("isabelle", "password", webClient)
            this.bjornToken = authenticateWith("bjorn", "password", webClient)
        }

        fun authenticateWith(userName: String, password: String, webClient: WebClient): KeycloakToken =
            webClient
                .post()
                .body(
                    BodyInserters
                        .fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", userName)
                        .with("password", password)
                ).retrieve()
                .bodyToMono<KeycloakToken>()
                .block()!!
    }
}
