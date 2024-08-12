package de.bashburg.springbook.catalogservice.web

import de.bashburg.springbook.catalogservice.domain.Book
import de.bashburg.springbook.catalogservice.domain.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("books")
class BookController(private val bookService: BookService) {
    @GetMapping
    fun getBooks(): Iterable<Book> = bookService.viewBookList()

    @GetMapping("{isbn}")
    fun getByIsbn(@PathVariable isbn: String) = bookService.viewBookDetails(isbn)

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@Valid @RequestBody book: Book) = bookService.addBookToCatalog(book)

    @DeleteMapping("{isbn}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable isbn: String) = bookService.removeBookFromCatalog(isbn)

    @PutMapping("{isbn}")
    fun put(@PathVariable isbn: String, @Valid @RequestBody book: Book) = bookService.editBookDetails(isbn, book)
}
