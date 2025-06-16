package edu.online.messenger.util;

import edu.online.messenger.model.entity.Address;
import lombok.Builder;

@Builder(setterPrefix = "with")
public class AddressTestBuilder {

    @Builder.Default
    private Long id = 10L;

    public Address buildAddress() {
        Address address = new Address();
        address.setId(id);
        return address;
    }
}