package de.bashburg.springbook.catalogservice.domain

import org.springframework.stereotype.Service

@Service
class BookService(val bookRepository: BookRepository) {
    fun viewBookList(): Iterable<Book> = bookRepository.findAll()

    fun viewBookDetails(isbn: String): Book =
        bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException(isbn)

    fun addBookToCatalog(book: Book): Book {
        if (bookRepository.existsByIsbn(book.isbn)) {
            throw BookAlreadyExistsException(book.isbn)
        }
        return bookRepository.save(book)
    }

    fun removeBookFromCatalog(isbn: String) = bookRepository.deleteByIsbn(isbn)

    fun editBookDetails(isbn: String, book: Book) {
        val bookToUpdate = bookRepository.findByIsbn(isbn)?.let {
            Book(it.isbn, book.title, book.author, book.price)
        } ?: book

        bookRepository.save(bookToUpdate)
    }
}