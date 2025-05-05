package edu.online.messenger.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressFilterDto {

    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String house;
    private String housing;
    private String apartment;
}