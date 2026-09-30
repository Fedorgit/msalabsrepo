package org.msa.library.dto.book;

import org.msa.library.entity.Category;

import java.time.Instant;
import java.util.Set;

public record BookUpdateDto(
        Long id,
        String name,
        String description,
        Instant publicationDate,
        Set<Long> authorsId,
        Set<Integer> categoriesId
) {
}