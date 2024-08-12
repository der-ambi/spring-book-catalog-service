package de.bashburg.springbook.catalogservice.domain

class BookAlreadyExistsException(isbn: String) : RuntimeException("A book with $isbn already exists")
