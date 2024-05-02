package com.example.library.controller;

import com.example.library.exception.*;
import com.example.library.model.Book;
import com.example.library.model.MessageResponse;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The BookController class handles HTTP requests related to books.
 */
@RestController
@RequestMapping("/api/books")
@Api(tags = "Book Management")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Retrieves all books.
     *
     * @return ResponseEntity containing the list of books and HttpStatus OK if successful
     */
    @GetMapping
    @ApiOperation(value = "Get all books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Retrieves a book by ID.
     *
     * @param id the ID of the book to retrieve
     * @return ResponseEntity containing the book and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the book does not exist
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new book.
     *
     * @param book the book to create
     * @return ResponseEntity containing the created book and HttpStatus CREATED if successful,
     *         or HttpStatus BAD_REQUEST if the request is invalid
     */
    @PostMapping
    @ApiOperation(value = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book newBook = bookService.save(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getQuantity());
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (BookAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    /**
     * Updates an existing book.
     *
     * @param id   the ID of the book to update
     * @param book the updated book information
     * @return ResponseEntity containing the updated book and HttpStatus OK if successful,
     *         or HttpStatus BAD_REQUEST if the request is invalid
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.update(id, book.getIsbn(), book.getTitle(), book.getAuthor(), book.getQuantity());
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (BookAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    /**
     * Deletes a book by ID.
     *
     * @param id the ID of the book to delete
     * @return ResponseEntity with a success message and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the book does not exist
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a book by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book deleted successfully"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Book successfully deleted"), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
