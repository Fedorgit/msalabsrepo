package org.msa.library.dto.user;

import org.msa.library.entity.Role;

import java.time.Instant;

public record UserGetInfoDto(String name,
                             String surname,
                             String login,
                             Instant createAt,
                             String role) {
}
