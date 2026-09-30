package org.msa.library.dto.book;

import org.msa.library.dto.author.AuthorGetInfoDto;
import org.msa.library.dto.category.CategoryCrudDto;

import java.time.Instant;
import java.util.Set;

public record BookGetTitleInfoDto(
        Long id,
        String name,
        Instant publicationDate,
        Set<AuthorGetInfoDto> authors,
        Set<CategoryCrudDto> categories
) {
}
