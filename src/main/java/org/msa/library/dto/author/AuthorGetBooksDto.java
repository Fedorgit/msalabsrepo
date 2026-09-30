package org.msa.library.dto.author;

import org.msa.library.entity.Book;

import java.util.Set;

public record AuthorGetBooksDto(Set<Book> books) {
}
