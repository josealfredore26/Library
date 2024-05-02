package com.example.library.service;

import com.example.library.exception.*;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * The LoanService class provides business logic for managing Loan entities.
 * It handles operations such as finding, saving, updating, and deleting loans.
 */
@Service
public class LoanService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Retrieves all loans from the database.
     *
     * @return a list of all loans
     */
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    /**
     * Finds a loan by its ID.
     *
     * @param id the ID of the loan to find
     * @return the loan if found
     * @throws LoanNotFoundException if the loan is not found
     */
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
    }

    /**
     * Saves a new loan to the database.
     *
     * @param userId    the ID of the user borrowing the book
     * @param bookId    the ID of the book being borrowed
     * @param startDate the start date of the loan
     * @param endDate   the end date of the loan
     * @return the saved loan
     * @throws UserNotFoundException        if the user is not found
     * @throws BookNotFoundException        if the book is not found
     * @throws NoBookAvailableException     if the book is not available for loan
     * @throws InconsistentDatesException   if the start date is after the end date
     */
    public Loan save(Long userId, Long bookId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            Book book = bookRepository.findById(bookId).orElse(null);
            if(book != null) {
                if (book.getQuantity() >=1) {
                    if(startDate.isBefore(endDate)) {
                        return loanRepository.save(new Loan(user, book, startDate, endDate));
                    } else {
                        throw new InconsistentDatesException("Start date must be before end date");
                    }
                } else {
                    throw new NoBookAvailableException("No book available");
                }
            } else {
                throw new BookNotFoundException("Book not found");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Updates an existing loan in the database.
     *
     * @param id        the ID of the loan to update
     * @param userId    the new ID of the user borrowing the book
     * @param bookId    the new ID of the book being borrowed
     * @param startDate the new start date of the loan
     * @param endDate   the new end date of the loan
     * @return the updated loan
     * @throws LoanNotFoundException      if the loan to update is not found
     * @throws UserNotFoundException      if the user is not found
     * @throws BookNotFoundException      if the book is not found
     * @throws InconsistentDatesException if the start date is after the end date
     */
    public Loan update(Long id, Long userId, Long bookId, LocalDate startDate, LocalDate endDate) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if(loan != null) {
            User user = userRepository.findById(userId).orElse(null);
            if(user != null) {
                Book book = bookRepository.findById(bookId).orElse(null);
                if(book != null) {
                    if(startDate.isBefore(endDate)) {
                        loan.setUser(user);
                        loan.setBook(book);
                        loan.setStartDate(startDate);
                        loan.setEndDate(endDate);
                        return loanRepository.save(loan);
                    } else {
                        throw new InconsistentDatesException("Start date must be before end date");
                    }
                } {
                    throw new BookNotFoundException("Book not found");
                }
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new LoanNotFoundException("Loan not found");
        }
    }

    /**
     * Deletes a loan from the database.
     *
     * @param id the ID of the loan to delete
     * @throws LoanNotFoundException if the loan to delete is not found
     */
    public void delete(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if(loan != null) {
            loan.setFinalized(true);
            loanRepository.save(loan);
        } else {
            throw new LoanNotFoundException("Loan not found");
        }
    }

}
