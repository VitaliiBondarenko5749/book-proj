package edu.cs.bookproj.repository;

import edu.cs.bookproj.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
@author Vitalii
@project book-proj
@interface BookRepository
@version 1.0.0
@since 13.09.24 - 9:45
*/

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
}