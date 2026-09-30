package org.msa.library;

import org.junit.jupiter.api.Test;
import org.msa.library.entity.Book;
import org.msa.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    private Book createBook(String name) {
        Book book = new Book();

        book.setName(name);
        book.setDescription("Test description");
        book.setFilePath(name + ".pdf");
        book.setPublicationDate(Instant.now());
        book.setCreatedAt(Instant.now());

        return book;
    }

    @Test
    void saveBookTest() {
        Book book = createBook("Book 1");

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    void findBookByIdTest() {
        Book book = bookRepository.save(createBook("Book 2"));

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getName()).isEqualTo("Book 2");
    }

    @Test
    void findAllBooksTest() {
        bookRepository.save(createBook("Book 3"));
        bookRepository.save(createBook("Book 4"));

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findByNameTest() {
        bookRepository.save(createBook("Java Programming"));

        Optional<Book> book = bookRepository.findByName("Java Programming");

        assertThat(book).isPresent();
        assertThat(book.get().getName()).isEqualTo("Java Programming");
    }

    @Test
    void findLastTenBooksTest() {
        for (int i = 1; i <= 12; i++) {
            Book book = createBook("Book " + i);
            book.setCreatedAt(Instant.now().plusSeconds(i));
            bookRepository.save(book);
        }

        List<Book> books = bookRepository.findTop10ByOrderByCreatedAtDesc();

        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("Book 12");
    }

    @Test
    void updateBookTest() {
        Book book = bookRepository.save(createBook("Old name"));

        book.setName("New name");

        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getName()).isEqualTo("New name");
    }

    @Test
    void deleteBookTest() {
        Book book = bookRepository.save(createBook("Book to delete"));

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}