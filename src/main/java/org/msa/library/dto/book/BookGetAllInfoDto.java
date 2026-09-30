package org.msa.library.dto.book;

import org.msa.library.dto.author.AuthorGetInfoDto;
import org.msa.library.dto.category.CategoryCrudDto;

import java.time.Instant;
import java.util.Set;

public record BookGetAllInfoDto(
        Long id,
        String name,
        String description,
        Instant publicationDate,
        Set<AuthorGetInfoDto> authors,
        Set<CategoryCrudDto> categories
) {
}
