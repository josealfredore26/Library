package com.example.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Book entity represents books available in the library.
 * It contains an auto-generated ID, an ISBN, a title, an author, and a quantity.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "loan_books")
public class Book {

    /**
     * The unique identifier for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ISBN (International Standard Book Number) of the book.
     */
    private String isbn;

    /**
     * The title of the book.
     */
    private String title;

    /**
     * The author of the book.
     */
    private String author;

    /**
     * The quantity of available copies of the book.
     */
    private int quantity;

    /**
     * Constructs a new Book object with the given ISBN, title, author, and quantity.
     * @param isbn The ISBN of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param quantity The quantity of available copies of the book.
     */
    public Book(String isbn, String title, String author, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

}

