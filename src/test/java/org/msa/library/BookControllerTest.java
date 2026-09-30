package org.msa.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.entity.Author;
import org.msa.library.entity.Book;
import org.msa.library.entity.Category;
import org.msa.library.mapper.BookMapper;
import org.msa.library.repository.AuthorRepository;
import org.msa.library.repository.BookRepository;
import org.msa.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Faker faker;

    private Author testAuthor;
    private Category testCategory;
    private Book testBook;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();

        testAuthor = Instancio.of(Author.class)
                .ignore(Select.field(Author::getId))
                .ignore(Select.field(Author::getBooks))
                .generate(Select.all(Instant.class), gen -> gen.temporal().instant()
                        .past()
                        .truncatedTo(ChronoUnit.SECONDS))
                .supply(
                        Select.field(Author::getName),
                        () -> faker.name().firstName()
                )
                .supply(
                        Select.field(Author::getSurname),
                        () -> faker.name().lastName()
                )
                .supply(
                        Select.field(Author::getPatronymic),
                        () -> faker.name().firstName()
                )
                .create();

        testCategory = Instancio.of(Category.class)
                .ignore(Select.field(Category::getId))
                .ignore(Select.field(Category::getBooks))
                .supply(
                        Select.field(Category::getCategory),
                        () -> faker.book().genre() + " " + faker.internet().uuid()
                )
                .create();

        testAuthor = authorRepository.save(testAuthor);
        testCategory = categoryRepository.save(testCategory);

        testBook = Instancio.of(Book.class)
                .ignore(Select.field(Book::getId))
                .ignore(Select.field(Book::getCreatedAt))
                .generate(Select.all(Instant.class), gen -> gen.temporal().instant()
                        .past()
                        .truncatedTo(ChronoUnit.SECONDS))
                .supply(
                        Select.field(Book::getName),
                        () -> faker.book().title() + " " + faker.internet().uuid()
                )
                .supply(
                        Select.field(Book::getDescription),
                        () -> faker.lorem().paragraph()
                )
                .supply(
                        Select.field(Book::getFilePath),
                        () -> faker.internet().uuid() + ".pdf"
                )
                .supply(
                        Select.field(Book::getAuthors),
                        () -> new HashSet<>(Set.of(testAuthor))
                )
                .supply(
                        Select.field(Book::getCategories),
                        () -> new HashSet<>(Set.of(testCategory))
                )
                .create();

        testAuthor.getBooks().add(testBook);
        testBook.getAuthors().add(testAuthor);

        testCategory.getBooks().add(testBook);
        testBook.getCategories().add(testCategory);
    }



    @Test
    public void testCreateBook() throws Exception {

        BookCreateDto dto = new BookCreateDto(
                testBook.getName(),
                testBook.getDescription(),
                testBook.getPublicationDate(),
                Set.of(testAuthor.getId()),
                Set.of(testCategory.getId())
        );

        var request = post("/api/v1/books/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var book = bookRepository.findByName(dto.name()).get();

        assertNotNull(book);
        assertThat(book.getName()).isEqualTo(dto.name());
        assertThat(book.getDescription()).isEqualTo(dto.description());
        assertThat(book.getPublicationDate().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(dto.publicationDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Test getting last 10 books")
    public void testGetLastTenBooks() throws Exception {

        for (int i = 0; i < 11; i++) {
            final int index = i;

            Book book = Instancio.of(Book.class)
                    .ignore(Select.field(Book::getId))
                    .supply(
                            Select.field(Book::getName),
                            () -> "Book " + index
                    )
                    .supply(
                            Select.field(Book::getFilePath),
                            () -> faker.internet().uuid() + ".pdf"
                    )
                    .supply(
                            Select.field(Book::getCreatedAt),
                            () -> Instant.now().plusSeconds(index)
                    )
                    .supply(
                            Select.field(Book::getAuthors),
                            () -> new HashSet<>(Set.of(testAuthor))
                    )
                    .supply(
                            Select.field(Book::getCategories),
                            () -> new HashSet<>(Set.of(testCategory))
                    )
                    .create();

            bookRepository.save(book);
        }

        var result = mockMvc.perform(
                        get("/api/v1/books")
                )
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        List<BookGetTitleInfoDto> books = om.readValue(
                body,
                om.getTypeFactory().constructCollectionType(
                        List.class,
                        BookGetTitleInfoDto.class
                )
        );

        assertThat(books).hasSize(10);

        assertThat(books.get(0).name())
                .isEqualTo("Book 10");

        assertThat(books.get(9).name())
                .isEqualTo("Book 1");
    }

    @Test
    public void testGetBook() throws Exception {

        testBook = bookRepository.save(testBook);

        var result = mockMvc.perform(
                        get("/api/v1/books/{id}", testBook.getId())
                )
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isObject();

        assertThatJson(body).and(
                json -> json.node("id")
                        .isEqualTo(testBook.getId()),

                json -> json.node("name")
                        .isEqualTo(testBook.getName()),

                json -> json.node("description")
                        .isEqualTo(testBook.getDescription())
        );
    }

    @Test
    public void testUpdateBook() throws Exception {

        testBook = bookRepository.save(testBook);

        BookUpdateDto dto = new BookUpdateDto(
                testBook.getId(),
                "Updated book",
                "Updated description",
                testBook.getPublicationDate(),
                Set.of(testAuthor.getId()),
                Set.of(testCategory.getId())
        );

        var request = put(
                "/api/v1/books/{id}",
                testBook.getId()
        )
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Book book = bookRepository
                .findById(testBook.getId())
                .orElseThrow();

        assertThat(book.getName())
                .isEqualTo(dto.name());

        assertThat(book.getDescription())
                .isEqualTo(dto.description());

        assertThat(book.getPublicationDate().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(dto.publicationDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testDeleteBook() throws Exception {

        testBook = bookRepository.save(testBook);

        Long id = testBook.getId();

        mockMvc.perform(
                        delete("/api/v1/books/{id}", id)
                )
                .andExpect(status().isNoContent());

        assertThat(
                bookRepository.existsById(id)
        ).isFalse();
    }
}