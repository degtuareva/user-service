package edu.online.messenger.model.entity.dto;

public record AddressFilterDto(
        String country,
        String postalCode,
        String city,
        String street,
        String house,
        String housing,
        String apartment
) {
}