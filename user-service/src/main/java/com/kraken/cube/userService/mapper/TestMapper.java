package com.kraken.cube.userService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.userService.dto.UserDto;
import com.kraken.cube.userService.model.User;

@Mapper(componentModel = "spring")
public interface TestMapper {
    
    UserDto toUserDto(UserRegisteredDto dto);

    
    
    @Mapping(target = "userId", ignore = true)
    User toUser(UserDto dto);
}