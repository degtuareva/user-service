package edu.online.messenger.util;

import edu.online.messenger.model.dto.AddressFilterDto;
import edu.online.messenger.model.entity.Address;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class AddressSpecification<E> {

    public static Specification<Address> findAll(AddressFilterDto filter) {

        Specification<Address> spec = Specification.where(null);
        if (StringUtils.isNotEmpty(filter.getCountry())) {
            spec = spec.and(findByCountry(filter.getCountry()));
        }
        if (StringUtils.isNotEmpty(filter.getPostalCode())) {
            spec = spec.and(findByPostalCode(filter.getPostalCode()));
        }
        if (StringUtils.isNotEmpty(filter.getCity())) {
            spec = spec.and(findByCity(filter.getCity()));
        }
        if (StringUtils.isNotEmpty(filter.getStreet())) {
            spec = spec.and(findByStreet(filter.getStreet()));
        }
        if (StringUtils.isNotEmpty(filter.getHouse())) {
            spec = spec.and(findByHouse(filter.getHouse()));
        }
        if (StringUtils.isNotEmpty(filter.getHousing())) {
            spec = spec.and(findByHousing(filter.getHousing()));
        }
        if (StringUtils.isNotEmpty(filter.getApartment())) {
            spec = spec.and(findByApartment(filter.getApartment()));
        }
        return spec;
    }

    public static Specification<Address> findByCountry(String country) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("country"), country);
    }

    public static Specification<Address> findByPostalCode(String postalCode) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("postalCode"), postalCode);
    }

    public static Specification<Address> findByCity(String city) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("city"), city);
    }

    public static Specification<Address> findByStreet(String street) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("street"), street);
    }

    public static Specification<Address> findByHouse(String house) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("house"), house);
    }

    public static Specification<Address> findByHousing(String housing) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("housing"), housing);
    }

    public static Specification<Address> findByApartment(String apartment) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("apartment"), apartment);
    }
}
