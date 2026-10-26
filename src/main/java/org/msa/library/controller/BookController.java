package org.msa.library.controller;

import lombok.RequiredArgsConstructor;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetAllInfoDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<BookGetTitleInfoDto> getLastTen() {
        return bookService.getLastTen();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BookGetAllInfoDto create(
            @RequestBody BookCreateDto dto
    ) {
        return bookService.create(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookGetAllInfoDto getById(
            @PathVariable Long id
    ) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookGetAllInfoDto update(
            @RequestBody BookUpdateDto dto
    ) {
        return bookService.update(dto.id(), dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        bookService.delete(id);
    }
}