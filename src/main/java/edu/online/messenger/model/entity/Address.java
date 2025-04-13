package edu.online.messenger.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    int apartment;

    String housing;

    @Column(nullable = false)
    int house;

    @Column(nullable = false)
    String street;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String postalCode;

    @Column(nullable = false)
    String country;
}
