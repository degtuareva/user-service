package edu.online.messenger.mapper;

import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(User user);
}
