package com.example.library.controller;

import com.example.library.exception.BookAlreadyExistsException;
import com.example.library.exception.InvalidDataException;
import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book newBook = bookService.save(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getQuantity());
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BookAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.update(id, book.getIsbn(), book.getTitle(), book.getAuthor(), book.getQuantity());
            if (updatedBook != null) {
                return new ResponseEntity<>(updatedBook, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BookAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
