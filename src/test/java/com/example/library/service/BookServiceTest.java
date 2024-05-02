package com.example.library.service;

import com.example.library.exception.BookAlreadyExistsException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.InvalidDataException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The BookServiceTest class contains unit tests for the Book1Service class.
 */
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_NoBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of());

        // Act
        List<Book> result = bookService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_WithBooks() {
        // Arrange
        List<Book> books = List.of(
                new Book("1234567890", "Book 1", "Author 1", 1),
                new Book("0987654321", "Book 2", "Author 2", 2)
        );
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(books.get(0), result.get(0));
        assertEquals(books.get(1), result.get(1));
    }

    @Test
    void testFindById_BookExists() {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    void testFindById_BookNotFound() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> bookService.findById(id));
    }

    @Test
    void testSave_ValidData() {
        // Arrange
        String isbn = "1234567890";
        String title = "Book Title";
        String author = "Author";
        int quantity = 1;
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book result = bookService.save(isbn, title, author, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(quantity, result.getQuantity());
    }

    @Test
    void testSave_BookAlreadyExists() {
        // Arrange
        String isbn = "1234567890";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(new Book()));

        // Act and Assert
        assertThrows(BookAlreadyExistsException.class, () -> bookService.save(isbn, "Book Title", "Author", 1));
    }

    @Test
    void testSave_InvalidData() {
        // Act and Assert
        assertThrows(InvalidDataException.class, () -> bookService.save(null, "Book Title", "Author", 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("", "Book Title", "Author", 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("1234567890", null, "Author", 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("1234567890", "", "Author", 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("1234567890", "Book Title", null, 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("1234567890", "Book Title", "", 1));
        assertThrows(InvalidDataException.class, () -> bookService.save("1234567890", "Book Title", "Author", 0));
    }

    @Test
    void testUpdate_ValidData() {
        // Arrange
        Long id = 1L;
        String isbn = "1234567890";
        String title = "Book Title";
        String author = "Author";
        int quantity = 1;
        Book existingBook = new Book("1234567890", "Book 1", "Author 1", 4);
        existingBook.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book result = bookService.update(id, isbn, title, author, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(isbn, result.getIsbn());
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(quantity, result.getQuantity());
    }

    @Test
    void testUpdate_BookNotFound() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> bookService.update(id, "1234567890", "Book Title", "Author", 1));
    }

    @Test
    void testUpdate_BookAlreadyExistsWithSameIsbn() {
        // Arrange
        Long id = 1L;
        String isbn = "1234567890";
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(new Book()));

        // Act and Assert
        assertThrows(BookAlreadyExistsException.class, () -> bookService.update(id, isbn, "Book Title", "Author", 1));
    }

    @Test
    void testDelete_BookExists() {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        bookService.delete(id);

        // Assert: No exceptions should be thrown
    }

    @Test
    void testDelete_BookNotFound() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> bookService.delete(id));
    }
}
