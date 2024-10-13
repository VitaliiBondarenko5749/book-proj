package edu.cs.bookproj.service;

import edu.cs.bookproj.model.Book;
import edu.cs.bookproj.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
@author Vitalii
@project book-proj
@class BookService
@version 1.0.0
@since 13.09.24 - 9:40
*/

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(String id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}