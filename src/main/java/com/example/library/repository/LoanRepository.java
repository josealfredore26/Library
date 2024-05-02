package com.example.library.repository;

import com.example.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The LoanRepository interface provides access to the database for Loan entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations.
 */
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
