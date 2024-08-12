package de.bashburg.springbook.catalogservice.web

import de.bashburg.springbook.catalogservice.domain.BookNotFoundException
import de.bashburg.springbook.catalogservice.domain.BookService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
class BookControllerMvcTests() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    @Test
    fun `when get book not existing then should return 404`() {
        val isbn = "7373737313940"
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException::class.java)

        mockMvc
            .perform(get("/books/$isbn"))
            .andExpect(status().isNotFound)
    }
}