package org.msa.library.dto.user;

public record UserCreateDto(
        String name,
        String surname,
        String login,
        String password) {
}
