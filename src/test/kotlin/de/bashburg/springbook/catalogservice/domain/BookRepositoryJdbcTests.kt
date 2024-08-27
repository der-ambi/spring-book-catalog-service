package de.bashburg.springbook.catalogservice.domain

import de.bashburg.springbook.catalogservice.config.DataConfig
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import java.time.Instant
import java.time.temporal.ChronoUnit.MINUTES
import kotlin.test.Test


@DataJdbcTest
@Import(DataConfig::class)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var jdbcAggregateTemplate: JdbcAggregateTemplate

    @Test
    fun `find book by isbn when existing`() {
        val isbn = "1234561237"
        val book = Book.of(isbn, "Title", "Author", "Polarsophia", 9.90)
        jdbcAggregateTemplate.insert(book)

        val actualBook = bookRepository.findByIsbn("1234561237")

        assertThat(actualBook).isNotNull
        assertThat(actualBook?.isbn).isEqualTo(book.isbn)
        assertThat(actualBook?.title).isEqualTo(book.title)
    }

    @Test
    fun `when create book not authenticated then no audit metadata is written`() {
        val bookToCreate = Book.of("1234561237", "Title", "Author", "Polarsophia", 9.90)
        val createdBook = bookRepository.save(bookToCreate)

        assertThat(createdBook.createdBy).isNull()
        assertThat(createdBook.lastModifiedBy).isNull()
        assertThat(createdBook.createdDate).isCloseTo(Instant.now(), within(1, MINUTES))
    }

    @Test
    @WithMockUser("john")
    fun `when create book authenticated then audit metadata is written`() {
        val bookToCreate = Book.of("1234561237", "Title", "Author", "Polarsophia", 9.90)
        val createdBook = bookRepository.save(bookToCreate)

        assertThat(createdBook.createdBy).isEqualTo("john")
        assertThat(createdBook.lastModifiedBy).isEqualTo("john")
        assertThat(createdBook.lastModifiedDate).isCloseTo(Instant.now(), within(1, MINUTES))
    }
}