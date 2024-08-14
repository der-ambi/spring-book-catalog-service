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
        bookRepository.deleteAll()
        val book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", null, 9.9)
        val book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", null, 12.9)
        val book3 = Book.of("1234567893", "Aurora Nights", "Chris Van Allsburg", "Polarsophia", 11.9)

        bookRepository.saveAll(listOf(book1, book2, book3))
    }
}