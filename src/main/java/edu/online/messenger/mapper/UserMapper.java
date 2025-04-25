package edu.online.messenger.mapper;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
