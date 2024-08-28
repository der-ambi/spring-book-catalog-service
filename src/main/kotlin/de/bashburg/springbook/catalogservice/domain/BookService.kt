package de.bashburg.springbook.catalogservice.domain

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class BookService(val bookRepository: BookRepository) {
    fun viewBookList(): Iterable<Book> {
        logger.info { "Fetching the list of books in the catalog" }
        return bookRepository.findAll()
    }

    fun viewBookDetails(isbn: String): Book =
        bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException(isbn)

    fun addBookToCatalog(book: Book): Book {
        if (bookRepository.existsByIsbn(book.isbn)) {
            logger.warn { "Book with ISBN ${book.isbn} could not be added as it already exists" }
            throw BookAlreadyExistsException(book.isbn)
        }
        logger.info { "Added book $book to catalog" }
        return bookRepository.save(book)
    }

    fun removeBookFromCatalog(isbn: String) {
        bookRepository.deleteByIsbn(isbn)
        logger.info { "Book with ISBN $isbn was deleted from the catalog" }
    }

    fun editBookDetails(isbn: String, book: Book) {
        val bookToUpdate = bookRepository.findByIsbn(isbn)?.let {
            Book(
                id = it.id,
                version = book.version,
                isbn = it.isbn,
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price,
                lastModifiedDate = book.lastModifiedDate,
                lastModifiedBy = book.lastModifiedBy,
                createdDate = book.createdDate,
                createdBy = book.createdBy
            )
        } ?: book
        logger.info { "Book with ISBN $isbn was updated: $book" }
        bookRepository.save(bookToUpdate)
    }
}