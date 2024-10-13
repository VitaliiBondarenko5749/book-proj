package edu.cs.bookproj.repositorytests;

import edu.cs.bookproj.model.Book;
import edu.cs.bookproj.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
@author Vitalii
@project book-proj
@class BookRepositoryTest
@version 1.0.0
@since 13.10.24 - 12:22
*/

@DataMongoTest
@ActiveProfiles("test") // Використовую профіль "test" для ізоляції від основної бази
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // 1. Перевірка створення нової книги (CREATE)
    @Test
    public void shouldSaveNewBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(19.99);

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Test Book");
    }

    // 2. Перевірка знаходження книги за ID (READ)
    @Test
    public void shouldFindBookById() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(19.99);
        Book savedBook = bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Test Book");
    }

    // 3. Перевірка оновлення книги (UPDATE)
    @Test
    public void shouldUpdateBook() {
        Book book = new Book();
        book.setTitle("Original Title");
        book.setAuthor("Original Author");
        book.setPrice(10.0);
        Book savedBook = bookRepository.save(book);

        // Оновлення книги
        savedBook.setTitle("Updated Title");
        savedBook.setPrice(20.0);
        Book updatedBook = bookRepository.save(savedBook);

        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedBook.getPrice()).isEqualTo(20.0);
    }

    // 4. Перевірка видалення книги (DELETE)
    @Test
    public void shouldDeleteBook() {
        Book book = new Book();
        book.setTitle("Book to delete");
        book.setAuthor("Author");
        book.setPrice(15.0);
        Book savedBook = bookRepository.save(book);

        bookRepository.deleteById(savedBook.getId());

        Optional<Book> deletedBook = bookRepository.findById(savedBook.getId());
        assertThat(deletedBook).isNotPresent();
    }

    // 5. Перевірка знаходження всіх книг (READ ALL)
    @Test
    public void shouldFindAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setPrice(10.0);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setPrice(20.0);

        bookRepository.deleteAll();

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getTitle).containsExactlyInAnyOrder("Book 1", "Book 2");
    }

    // 6. Перевірка видалення всіх книг (DELETE ALL)
    @Test
    public void shouldDeleteAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setPrice(10.0);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setPrice(20.0);

        bookRepository.save(book1);
        bookRepository.save(book2);

        bookRepository.deleteAll();

        List<Book> books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }

    // 7. Перевірка пошуку книги за назвою
    @Test
    public void shouldFindBookByTitle() {
        Book book = new Book();
        book.setTitle("Unique Title");
        book.setAuthor("Test Author");
        book.setPrice(19.99);
        bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Unique Title");
    }

    // 8. Перевірка існування книги за ID
    @Test
    public void shouldCheckIfBookExistsById() {
        Book book = new Book();
        book.setTitle("Check Exists");
        book.setAuthor("Author");
        book.setPrice(25.0);
        Book savedBook = bookRepository.save(book);

        boolean exists = bookRepository.existsById(savedBook.getId());
        assertThat(exists).isTrue();
    }

    // 9. Перевірка знаходження книги за неіснуючим ID
    @Test
    public void shouldReturnEmptyForNonExistingId() {
        Optional<Book> book = bookRepository.findById("non-existing-id");
        assertThat(book).isNotPresent();
    }

    // 10. Перевірка кількості записів у базі (COUNT)
    @Test
    public void shouldCountBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setPrice(10.0);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setPrice(20.0);

        bookRepository.deleteAll();

        bookRepository.save(book1);
        bookRepository.save(book2);

        long count = bookRepository.count();
        assertThat(count).isEqualTo(2);
    }
}