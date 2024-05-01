package com.example.library.controller;

import com.example.library.model.Loan;
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
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.findById(id);
        if (loan != null) {
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan newLoan = loanService.save(loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
        if (newLoan != null) {
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        Loan updatedLoan = loanService.update(id, loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
        if (updatedLoan != null) {
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        boolean deleted = loanService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
