package edu.online.messenger.mapper;

import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "user.id", target = "userId")
    AddressDto toDto(Address address);

}

