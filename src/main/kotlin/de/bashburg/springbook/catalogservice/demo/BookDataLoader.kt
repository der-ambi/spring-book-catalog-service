package de.bashburg.springbook.catalogservice.demo

import de.bashburg.springbook.catalogservice.domain.Book
import de.bashburg.springbook.catalogservice.domain.BookRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("testdata")
class BookDataLoader(val bookRepository: BookRepository) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadBookTestData() {
        val book1 = Book("1234567891", "Northern Lights", "Lyra Silverstar", 9.9)
        val book2 = Book("1234567892", "Polar Journey", "Iorek Polarson", 12.9)

        bookRepository.save(book1)
        bookRepository.save(book2)
    }
}