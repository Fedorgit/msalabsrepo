package org.msa.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetAllInfoDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.entity.Book;
import org.msa.library.mapper.BookMapper;
import org.msa.library.repository.BookRepository;
import org.msa.library.service.BookService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();

        book.setId(1L);
        book.setName("Java");
        book.setDescription("Java book");
        book.setFilePath("java.pdf");
        book.setPublicationDate(Instant.now());
        book.setCreatedAt(Instant.now());
    }

    @Test
    void createBookTest() {
        BookCreateDto dto = new BookCreateDto(
                "Java",
                "Java book",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        BookGetAllInfoDto resultDto = new BookGetAllInfoDto(
                1L,
                "Java",
                "Java book",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        given(bookMapper.map(dto)).willReturn(book);
        given(bookRepository.save(book)).willReturn(book);
        given(bookMapper.mapToAllInfo(book)).willReturn(resultDto);

        BookGetAllInfoDto result = bookService.create(dto);

        assertThat(result).isEqualTo(resultDto);

        verify(bookMapper, times(1)).map(dto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).mapToAllInfo(book);
    }

    @Test
    void getBookByIdTest() {
        BookGetAllInfoDto resultDto = new BookGetAllInfoDto(
                1L,
                "Java",
                "Java book",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        given(bookRepository.findById(1L))
                .willReturn(Optional.of(book));

        given(bookMapper.mapToAllInfo(book))
                .willReturn(resultDto);

        BookGetAllInfoDto result = bookService.getById(1L);

        assertThat(result).isEqualTo(resultDto);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).mapToAllInfo(book);
    }

    @Test
    void getLastTenBooksTest() {
        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setName("Spring");

        List<Book> books = List.of(book, secondBook);

        BookGetTitleInfoDto firstDto = new BookGetTitleInfoDto(
                1L,
                "Java",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        BookGetTitleInfoDto secondDto = new BookGetTitleInfoDto(
                2L,
                "Spring",
                null,
                Set.of(),
                Set.of()
        );

        given(bookRepository.findTop10ByOrderByCreatedAtDesc())
                .willReturn(books);

        given(bookMapper.mapToTitleInfo(book))
                .willReturn(firstDto);

        given(bookMapper.mapToTitleInfo(secondBook))
                .willReturn(secondDto);

        List<BookGetTitleInfoDto> result = bookService.getLastTen();

        assertThat(result)
                .hasSize(2)
                .containsExactly(firstDto, secondDto);

        verify(bookRepository, times(1))
                .findTop10ByOrderByCreatedAtDesc();

        verify(bookMapper, times(1))
                .mapToTitleInfo(book);

        verify(bookMapper, times(1))
                .mapToTitleInfo(secondBook);
    }

    @Test
    void updateBookTest() {
        BookUpdateDto dto = new BookUpdateDto(
                1L,
                "Updated Java",
                "Updated description",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        BookGetAllInfoDto resultDto = new BookGetAllInfoDto(
                1L,
                "Updated Java",
                "Updated description",
                book.getPublicationDate(),
                Set.of(),
                Set.of()
        );

        given(bookRepository.findById(1L))
                .willReturn(Optional.of(book));

        given(bookMapper.mapToAllInfo(book))
                .willReturn(resultDto);

        BookGetAllInfoDto result = bookService.update(1L, dto);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).update(dto, book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).mapToAllInfo(book);

        assertThat(result).isEqualTo(resultDto);
    }

    @Test
    void deleteBookTest() {
        given(bookRepository.existsById(1L))
                .willReturn(true);

        bookService.delete(1L);

        verify(bookRepository, times(1))
                .existsById(1L);

        verify(bookRepository, times(1))
                .deleteById(1L);
    }
}
