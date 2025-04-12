package edu.online.messenger.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {

    Long id;
    int apartment;
    String housing;
    int house;
    String street;
    String city;
    String postalCode;
    String country;
}
