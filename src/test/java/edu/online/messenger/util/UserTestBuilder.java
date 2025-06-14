package edu.online.messenger.util;

import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.entity.Address;
import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private Long userId = 1L;

    @Builder.Default
    private List<Address> addresses = List.of(createAddress(1L), createAddress(2L));

    @Builder.Default
    private List<AddressDto> addressDtoList = List.of(createAddressDto(1L), createAddressDto(2L));

    private static Address createAddress(Long id) {
        return new Address() {{
            setId(id);
        }};
    }

    private static AddressDto createAddressDto(Long id) {
        return new AddressDto() {{
            setId(id);
        }};
    }

    public List<Address> buildAddressList() {
        return addresses;
    }

    public List<AddressDto> buildAddressDtoList() {
        return addressDtoList;
    }
}