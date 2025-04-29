package edu.online.messenger.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
    Address toEntity(AddressDto dto);
    List<AddressDto> toDtoList(List<Address> addresses);
}
}
