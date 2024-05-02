package com.example.library.service;

import com.example.library.exception.BookAlreadyExistsException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.InvalidDataException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The BookService class provides business logic for managing Book entities.
 * It handles operations such as finding, saving, updating, and deleting books.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all books
     */
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Finds a book by its ID.
     *
     * @param id the ID of the book to find
     * @return the book if found
     * @throws BookNotFoundException if the book is not found
     */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found."));
    }

    /**
     * Saves a new book to the database.
     *
     * @param isbn     the ISBN of the book
     * @param title    the title of the book
     * @param author   the author of the book
     * @param quantity the quantity of the book
     * @return the saved book
     * @throws InvalidDataException      if the provided data is invalid
     * @throws BookAlreadyExistsException if a book with the same ISBN already exists
     */
    public Book save(String isbn, String title, String author, int quantity) {
        validateData(isbn, title, author, quantity);
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            throw new BookAlreadyExistsException("Book already exists");
        }

        return bookRepository.save(new Book(isbn, title, author, quantity));
    }

    /**
     * Updates an existing book in the database.
     *
     * @param id       the ID of the book to update
     * @param isbn     the new ISBN of the book
     * @param title    the new title of the book
     * @param author   the new author of the book
     * @param quantity the new quantity of the book
     * @return the updated book
     * @throws BookNotFoundException     if the book to update is not found
     * @throws InvalidDataException      if the provided data is invalid
     * @throws BookAlreadyExistsException if a book with the same ISBN already exists
     */
    public Book update(Long id, String isbn, String title, String author, int quantity) {
        Book book = findById(id);
        if (book != null) {
            validateData(isbn, title, author, quantity);
            Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
            if (optionalBook.isPresent()) {
                Book auxBook = optionalBook.get();
                if (!Objects.equals(book.getId(), id)) {
                    throw new BookAlreadyExistsException("Book already exists with the same ISBN");
                }
            }

            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);

            return bookRepository.save(book);
        } else {
            throw new BookNotFoundException("Book not found.");
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param id the ID of the book to delete
     * @throws BookNotFoundException if the book to delete is not found
     */
    public void delete(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            bookRepository.delete(book);
        } else {
            throw new BookNotFoundException("Book not found.");
        }
    }

    /**
     * Verifies the validity of book data.
     *
     * @param isbn     the ISBN of the book
     * @param title    the title of the book
     * @param author   the author of the book
     * @param quantity the quantity of the book
     * @throws InvalidDataException if the provided data is invalid
     */
    private void validateData(String isbn, String title, String author, int quantity) {
        if (isbn == null || isbn.isBlank() || isbn.isEmpty()) {
            throw new InvalidDataException("ISBN cannot be null, blank or empty");
        }
        if (title == null || title.isBlank() || title.isEmpty()) {
            throw new InvalidDataException("Title cannot be null, blank or empty");
        }
        if (author == null || author.isBlank() || author.isEmpty()) {
            throw new InvalidDataException("Author cannot be null, blank or empty");
        }
        if (quantity <= 0) {
            throw new InvalidDataException("Quantity must be greater than 0");
        }
    }

}

