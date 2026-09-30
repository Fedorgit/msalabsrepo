package org.msa.library.dto.user;

import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.entity.Book;

import java.util.Set;

public record UserGetBooksDto(
        Set<BookGetTitleInfoDto> userBooks
) {
}
