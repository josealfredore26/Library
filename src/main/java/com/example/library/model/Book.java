package com.example.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "loan_books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String title;
    private String author;

    private int quantity;

    public Book(String isbn, String title, String author, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

}
