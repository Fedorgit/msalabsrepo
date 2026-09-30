package org.msa.library.mapper;

import org.mapstruct.*;
import org.msa.library.dto.user.UserCreateDto;
import org.msa.library.dto.user.UserGetInfoDto;
import org.msa.library.dto.user.UserUpdateNameSurnameDto;
import org.msa.library.entity.User;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "role", source = "role.roleName")
    public UserGetInfoDto mapToInfo(User user);


    public UserCreateDto map(User user);


    public UserUpdateNameSurnameDto update(User user);

}
