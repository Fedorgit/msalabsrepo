package org.msa.library.dto.user;

import java.util.Set;

public record UserSetBooks(
        Set<Long> bookId
) {
}
