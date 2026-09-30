package org.msa.library.dto.author;


import java.time.LocalDate;

public record AuthorCreateDto(String name,
                              String surname,
                              String patronymic,
                              LocalDate birthDate,
                              LocalDate deadDate) {
}
