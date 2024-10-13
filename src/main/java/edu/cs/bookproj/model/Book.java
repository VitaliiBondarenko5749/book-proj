package edu.cs.bookproj.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
@author Vitalii
@project book-proj
@class Book
@version 1.0.0
@since 13.09.24 - 9:33
*/

@Data
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private double price;
}