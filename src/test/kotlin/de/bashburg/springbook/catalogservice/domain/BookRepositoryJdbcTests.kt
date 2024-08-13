package de.bashburg.springbook.catalogservice.domain

import de.bashburg.springbook.catalogservice.config.DataConfig
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test


@DataJdbcTest
@Import(DataConfig::class)
@ActiveProfiles("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryJdbcTests {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var jdbcAggregateTemplate: JdbcAggregateTemplate

    @Test
    fun `find book by isbn when existing`() {
        val isbn = "1234561237"
        val book = Book.of(isbn, "Title", "Author", 9.90)
        jdbcAggregateTemplate.insert(book)

        val actualBook = bookRepository.findByIsbn("1234561237")

        assertThat(actualBook).isNotNull
        assertThat(actualBook?.isbn).isEqualTo(book.isbn)
        assertThat(actualBook?.title).isEqualTo(book.title)
    }
}