package org.msa.library.mapper;

import org.mapstruct.*;
import org.msa.library.dto.category.CategoryCrudDto;
import org.msa.library.entity.Category;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category map(CategoryCrudDto dto);

    CategoryCrudDto map(Category category);



}
