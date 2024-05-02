package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The BookRepository interface provides access to the database for Book entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Retrieves a book by its ISBN.
     * @param isbn The ISBN of the book to retrieve.
     * @return An Optional containing the book with the given ISBN, or empty if not found.
     */
    Optional<Book> findByIsbn(String isbn);
}

