package org.msa.library.mapper;

import org.mapstruct.*;
import org.msa.library.dto.author.AuthorCreateDto;
import org.msa.library.dto.author.AuthorGetInfoDto;
import org.msa.library.dto.author.AuthorUpdateInfoDto;
import org.msa.library.entity.Author;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "books",ignore = true)
    Author map(AuthorCreateDto dto);

    AuthorGetInfoDto map(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "books",ignore = true)
    Author map(AuthorUpdateInfoDto dto);
}
