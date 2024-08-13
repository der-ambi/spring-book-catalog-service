package de.bashburg.springbook.catalogservice.domain

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<Book, Long> {
    fun findByIsbn(isbn: String): Book?
    fun existsByIsbn(isbn: String): Boolean

    @Modifying
    @Query("delete from book where isbn = :isbn")
    fun deleteByIsbn(isbn: String)
}