package com.example.library.controller;

import com.example.library.exception.*;
import com.example.library.model.Book;
import com.example.library.model.MessageResponse;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The BookController class handles HTTP requests related to books.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Retrieves all books.
     *
     * @return ResponseEntity containing the list of books and HttpStatus OK if successful
     */
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books")
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
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<?> getBookById(@Parameter(description = "ID of the book") @PathVariable Long id) {
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
    @Operation(summary = "Create a new book", description = "Creates a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Book already exists")
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
    @Operation(summary = "Update an existing book", description = "Updates an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Book already exists with provided ISBN")
    })
    public ResponseEntity<?> updateBook(@Parameter(description = "ID of the book") @PathVariable Long id, @RequestBody Book book) {
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
    @Operation(summary = "Delete a book by ID", description = "Deletes a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<?> deleteBook(@Parameter(description = "ID of the book") @PathVariable Long id) {
        try {
            bookService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Book successfully deleted"), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
