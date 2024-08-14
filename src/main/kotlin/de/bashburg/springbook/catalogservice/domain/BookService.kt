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
            Book(
                id = it.id,
                version = book.version,
                isbn = it.isbn,
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                price = book.price,
                lastModifiedDate = book.lastModifiedDate,
                createdDate = book.createdDate
            )
        } ?: book

        bookRepository.save(bookToUpdate)
    }
}