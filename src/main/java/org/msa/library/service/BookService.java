package org.msa.library.service;

import lombok.RequiredArgsConstructor;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetAllInfoDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.entity.Book;
import org.msa.library.mapper.BookMapper;
import org.msa.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookGetAllInfoDto create(BookCreateDto dto) {

        Book book = bookMapper.map(dto);

        bookRepository.save(book);

        return bookMapper.mapToAllInfo(book);
    }

    @Transactional(readOnly = true)
    public List<BookGetTitleInfoDto> getLastTen() {

        return bookRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(bookMapper::mapToTitleInfo)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookGetAllInfoDto getById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Book not found: " + id
                        )
                );

        return bookMapper.mapToAllInfo(book);
    }

    @Transactional
    public BookGetAllInfoDto update(Long id, BookUpdateDto dto) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Book not found: " + id
                        )
                );

        bookMapper.update(dto, book);

        bookRepository.save(book);

        return bookMapper.mapToAllInfo(book);
    }

    @Transactional
    public void delete(Long id) {

        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found: " + id
            );
        }

        bookRepository.deleteById(id);
    }
}