package com.example.library.service;

import com.example.library.exception.*;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoanServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_SuccessfulLoan() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);
        book.setQuantity(1);
        LocalDate startDate = LocalDate.of(2024, 5, 2);
        LocalDate endDate = LocalDate.of(2024, 5, 7);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Loan result = loanService.save(1L, 1L, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(book, result.getBook());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
    }

    @Test
    public void testSave_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> loanService.save(1L, 1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testSave_BookNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> loanService.save(1L, 1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testSave_NoBooksAvailable() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setQuantity(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act and Assert
        assertThrows(NoBookAvailableException.class, () -> loanService.save(1L, 1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testSave_InconsistentDates() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        // Act and Assert
        assertThrows(InconsistentDatesException.class, () -> loanService.save(1L, 1L, LocalDate.of(2020, 7, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testUpdate_SuccessfulUpdate() {
        // Arrange
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setFinalized(false);
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);
        LocalDate startDate = LocalDate.of(2024, 5, 2);
        LocalDate endDate = LocalDate.of(2024, 5, 7);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Loan result = loanService.update(1L, 1L, 1L, startDate, endDate);

        System.out.println(result);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(book, result.getBook());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
    }

    @Test
    public void testUpdate_LoanNotFound() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(LoanNotFoundException.class, () -> loanService.update(1L, 1L, 1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testUpdate_UserNotFound() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.of(new Loan()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> loanService.update(1L, 1L, 1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testUpdate_BookNotFound() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.of(new Loan()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> loanService.update(1L, 1L, 1L, LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 04)));
    }

    @Test
    public void testUpdate_InconsistentDates() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.of(new Loan()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        // Act and Assert
        assertThrows(InconsistentDatesException.class, () -> loanService.update(1L, 1L, 1L, LocalDate.of(2020, 07, 01), LocalDate.of(2020, 01, 04)));
    }

    @Test
    public void testDelete_SuccessfulDeletion() {
        // Arrange
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setFinalized(false);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Act
        loanService.delete(1L);

        // Assert
        assertTrue(loan.isFinalized());
    }

    @Test
    public void testDelete_LoanNotFound() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(LoanNotFoundException.class, () -> loanService.delete(1L));
    }
}
