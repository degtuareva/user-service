package edu.online.messenger.util;

import edu.online.messenger.model.dto.AddressDto;
import lombok.Builder;

@Builder(setterPrefix = "with")
public class AddressDtoTestBuilder {

    @Builder.Default
    private Long id = 10L;

    public AddressDto buildAddressDto() {
        AddressDto dto = new AddressDto();
        dto.setId(id);
        return dto;
    }
}