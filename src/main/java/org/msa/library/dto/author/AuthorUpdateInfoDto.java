package org.msa.library.dto.author;

import java.time.LocalDate;

public record AuthorUpdateInfoDto(
        Long id,
        String name,
        String surname,
        String patronymic,
        LocalDate birthDate,
        LocalDate deadDate
) {
}
