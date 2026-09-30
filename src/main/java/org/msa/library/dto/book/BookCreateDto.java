package org.msa.library.dto.book;

import java.time.Instant;
import java.util.Set;

public record BookCreateDto(
        String name,
        String description,
        Instant publicationDate,
        Set<Long> authorId,
        Set<Integer> categoriesId
) {
}
