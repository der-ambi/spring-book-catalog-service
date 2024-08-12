package de.bashburg.springbook.catalogservice.domain

class BookNotFoundException(isbn: String) : RuntimeException("The book with $isbn was not found")
