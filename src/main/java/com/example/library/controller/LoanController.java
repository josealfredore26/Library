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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The LoanController class handles HTTP requests related to loans.
 */
@RestController
@RequestMapping("/api/loans")
@Api(tags = "Loan Management")
public class LoanController {

    @Autowired
    private LoanService loanService;

    /**
     * Retrieves all loans.
     *
     * @return ResponseEntity containing the list of loans and HttpStatus OK if successful
     */
    @GetMapping
    @ApiOperation(value = "Get all loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.findAll();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    /**
     * Retrieves a loan by ID.
     *
     * @param id the ID of the loan to retrieve
     * @return ResponseEntity containing the loan and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the loan does not exist
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get loan by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved loan"),
            @ApiResponse(code = 404, message = "Loan not found")
    })
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        try {
            Loan loan = loanService.findById(id);
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new loan.
     *
     * @param loan the loan to create
     * @return ResponseEntity containing the created loan and HttpStatus CREATED if successful,
     *         or HttpStatus BAD_REQUEST if the request is invalid
     */
    @PostMapping
    @ApiOperation(value = "Create a new loan")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Loan created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        try {
            Loan newLoan = loanService.save(loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } catch (UserNotFoundException | BookNotFoundException | NoBookAvailableException | InconsistentDatesException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing loan.
     *
     * @param id   the ID of the loan to update
     * @param loan the updated loan information
     * @return ResponseEntity containing the updated loan and HttpStatus OK if successful,
     *         or HttpStatus BAD_REQUEST if the request is invalid
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing loan")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loan updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        try {
            Loan updatedLoan = loanService.update(id, loan.getUser().getId(), loan.getBook().getId(), loan.getStartDate(), loan.getEndDate());
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (LoanNotFoundException | UserNotFoundException | BookNotFoundException | InconsistentDatesException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a loan by ID.
     *
     * @param id the ID of the loan to delete
     * @return ResponseEntity with a success message and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the loan does not exist
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a loan by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loan deleted successfully"),
            @ApiResponse(code = 404, message = "Loan not found")
    })
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        try {
            loanService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Loan successfully deleted"), HttpStatus.OK);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
