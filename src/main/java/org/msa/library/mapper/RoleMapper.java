package org.msa.library.mapper;

import org.mapstruct.*;
import org.msa.library.dto.role.RoleCrudDto;
import org.msa.library.entity.Role;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    Role map(RoleCrudDto dto);

    RoleCrudDto map(Role role);

}
