package com.example.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * The Loan entity represents a loan of a book to a user.
 * It contains an auto-generated ID, references to the user and the book involved in the loan,
 * start and end dates of the loan, and a flag indicating if the loan has been finalized.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Loan {

    /**
     * The unique identifier for the loan.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who borrowed the book.
     */
    @OneToOne
    private User user;

    /**
     * The book being borrowed.
     */
    @OneToOne
    private Book book;

    /**
     * The start date of the loan.
     */
    private LocalDate startDate;

    /**
     * The end date of the loan.
     */
    private LocalDate endDate;

    /**
     * A flag indicating if the loan has been finalized.
     */
    private boolean finalized;

    /**
     * Constructs a new Loan object with the given user, book, start date, and end date.
     * @param user The user borrowing the book.
     * @param book The book being borrowed.
     * @param startDate The start date of the loan.
     * @param endDate The end date of the loan.
     */
    public Loan(User user, Book book, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.book = book;
        this.startDate = startDate;
        this.endDate = endDate;
        this.finalized = false;
    }

    /**
     * Returns a string representation of the Loan object.
     * @return A string representation of the Loan object.
     */
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

