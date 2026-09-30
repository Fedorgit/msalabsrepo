package org.msa.library.mapper;

import org.mapstruct.*;
import org.msa.library.dto.author.AuthorUpdateInfoDto;
import org.msa.library.dto.book.BookCreateDto;
import org.msa.library.dto.book.BookGetAllInfoDto;
import org.msa.library.dto.book.BookGetTitleInfoDto;
import org.msa.library.dto.book.BookUpdateDto;
import org.msa.library.entity.Author;
import org.msa.library.entity.Book;
import org.msa.library.mapper.util.ReferenceMapper;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                AuthorMapper.class,
                CategoryMapper.class,
                ReferenceMapper.class
        }
)
public interface BookMapper {

    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authors", source = "authorId")
    @Mapping(target = "categories", source = "categoriesId")
    Book map(BookCreateDto dto);

    BookCreateDto mapToBookCreate(Book book);

    BookGetAllInfoDto mapToAllInfo(Book book);

    BookGetTitleInfoDto mapToTitleInfo(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authors", source = "authorsId")
    @Mapping(target = "categories", source = "categoriesId")
    void update(BookUpdateDto dto, @MappingTarget Book book);
}
