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

import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
    }

    public Loan save(Long userId, Long bookId, Date startDate, Date endDate) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            Book book = bookRepository.findById(bookId).orElse(null);
            if(book != null) {
                if (book.getQuantity() >=1) {
                    if(startDate.before(endDate)) {
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

    public Loan update(Long id, Long userId, Long bookId, Date startDate, Date endDate) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if(loan != null) {
            User user = userRepository.findById(userId).orElse(null);
            if(user != null) {
                Book book = bookRepository.findById(bookId).orElse(null);
                if(book != null) {
                    if(startDate.before(endDate)) {
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
