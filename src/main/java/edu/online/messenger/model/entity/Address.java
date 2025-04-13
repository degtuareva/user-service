package edu.online.messenger.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "address")
@Builder
@Table(name = "Entity", schema = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "apartment")
    int apartment;

    @Column(name = "housing")
    String housing;

    @Column(name = "house", nullable = false)
    int house;

    @Column(name = "street", nullable = false)
    String street;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "postalCode", nullable = false)
    String postalCode;

    @Column(name = "country", nullable = false)
    String country;
}
