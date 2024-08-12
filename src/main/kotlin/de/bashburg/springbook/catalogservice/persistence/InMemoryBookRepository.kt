package de.bashburg.springbook.catalogservice.persistence

import de.bashburg.springbook.catalogservice.domain.Book
import de.bashburg.springbook.catalogservice.domain.BookRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryBookRepository : BookRepository {
    companion object {
        val books = ConcurrentHashMap<String, Book>()
    }

    override fun findAll(): List<Book> = books.values.toList()

    override fun existsByIsbn(isbn: String) = books.containsKey(isbn)

    override fun findByIsbn(isbn: String): Book? = books[isbn]

    override fun save(book: Book): Book = book.also {
        books[it.isbn] = it
    }

    override fun deleteByIsbn(isbn: String) {
        books.remove(isbn)
    }
}