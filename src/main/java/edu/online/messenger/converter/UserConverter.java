package edu.online.messenger.converter;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserDto toDto(User user);
}
