package com.example.library.controller;

import com.example.library.exception.*;
import com.example.library.model.Loan;
import com.example.library.model.MessageResponse;
import com.example.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.findAll();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        try {
            Loan loan = loanService.findById(id);
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        try {
            Loan newLoan = loanService.save(loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } catch (UserNotFoundException | BookNotFoundException | NoBookAvailableException | InconsistentDatesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        try {
            Loan updatedLoan = loanService.update(id, loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (LoanNotFoundException | UserNotFoundException | BookNotFoundException | InconsistentDatesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        try {
            loanService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Loan successfully deleted"), HttpStatus.OK);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
