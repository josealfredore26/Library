package com.example.library.service;

import com.example.library.exception.BookAlreadyExistsException;
import com.example.library.exception.InvalidDataException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book save(String isbn, String title, String author, int quantity) {
        verifyData(isbn, title, author, quantity);
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            throw new BookAlreadyExistsException("Book already exists");
        }

        return bookRepository.save(new Book(isbn, title, author, quantity));
    }

    public Book update(Long id, String isbn, String title, String author, int quantity) {
        Book book = findById(id);
        if (book != null) {
            verifyData(isbn, title, author, quantity);
            Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
            if (optionalBook.isPresent()) {
                Book auxBook = optionalBook.get();
                if (!Objects.equals(book.getId(), id)) {
                    throw new BookAlreadyExistsException("Book already exists with same isbn");
                }
            }

            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);

            return bookRepository.save(book);
        }
        return null;
    }

    public boolean delete(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            bookRepository.delete(book);
            return true;
        }
        return false;
    }

    private void verifyData(String isbn, String title, String author, int quantity) {
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
