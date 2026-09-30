package org.msa.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.msa.library.controller.BookController;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetAllInfoDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private BookService bookService;

    private BookGetAllInfoDto bookDto;

    private BookGetTitleInfoDto bookTitleDto;

    @BeforeEach
    void setUp() {
        Instant publicationDate = Instant.parse("2025-01-01T00:00:00Z");

        bookDto = new BookGetAllInfoDto(
                1L,
                "Java",
                "Java book",
                publicationDate,
                Set.of(),
                Set.of()
        );

        bookTitleDto = new BookGetTitleInfoDto(
                1L,
                "Java",
                publicationDate,
                Set.of(),
                Set.of()
        );
    }

    @Test
    @Disabled
    @Timeout(value = 2L,unit = TimeUnit.SECONDS)
    void createBookTest() throws Exception {

        BookCreateDto requestDto = new BookCreateDto(
                "Java",
                "Java book",
                bookDto.publicationDate(),
                Set.of(),
                Set.of()
        );

        given(bookService.create(any(BookCreateDto.class)))
                .willReturn(bookDto);

        mockMvc.perform(post("/api/v1/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java")))
                .andExpect(jsonPath("$.description", is("Java book")));

        verify(bookService, times(1))
                .create(any(BookCreateDto.class));
    }

    @Test
    void getLastTenBooksTest() throws Exception {

        given(bookService.getLastTen())
                .willReturn(List.of(bookTitleDto));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Java")));

        verify(bookService, times(1))
                .getLastTen();
    }

    @Test
    void getBookByIdTest() throws Exception {

        given(bookService.getById(1L))
                .willReturn(bookDto);

        mockMvc.perform(get("/api/v1/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java")))
                .andExpect(jsonPath("$.description", is("Java book")));

        verify(bookService, times(1))
                .getById(1L);
    }

    @Test
    void updateBookTest() throws Exception {

        BookUpdateDto requestDto = new BookUpdateDto(
                1L,
                "Updated Java",
                "Updated description",
                bookDto.publicationDate(),
                Set.of(),
                Set.of()
        );

        BookGetAllInfoDto updatedDto = new BookGetAllInfoDto(
                1L,
                "Updated Java",
                "Updated description",
                bookDto.publicationDate(),
                Set.of(),
                Set.of()
        );

        given(bookService.update(1L, requestDto))
                .willReturn(updatedDto);

        mockMvc.perform(put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Java")))
                .andExpect(jsonPath("$.description", is("Updated description")));

        verify(bookService, times(1))
                .update(1L, requestDto);
    }

    @Test
    void deleteBookTest() throws Exception {

        mockMvc.perform(delete("/api/v1/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(bookService, times(1))
                .delete(1L);
    }
}