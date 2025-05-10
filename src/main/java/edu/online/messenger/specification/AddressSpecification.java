package edu.online.messenger.specification;

import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.dto.AddressFilterDto;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class AddressSpecification {

    public static Specification<Address> findAll(AddressFilterDto filter) {

        Specification<Address> spec = Specification.where(null);
        if (StringUtils.isNotEmpty(filter.country())) {
            spec = spec.and(findByCountry(filter.country()));
        }
        if (StringUtils.isNotEmpty(filter.postalCode())) {
            spec = spec.and(findByPostalCode(filter.postalCode()));
        }
        if (StringUtils.isNotEmpty(filter.city())) {
            spec = spec.and(findByCity(filter.city()));
        }
        if (StringUtils.isNotEmpty(filter.street())) {
            spec = spec.and(findByStreet(filter.street()));
        }
        if (StringUtils.isNotEmpty(filter.house())) {
            spec = spec.and(findByHouse(filter.house()));
        }
        if (StringUtils.isNotEmpty(filter.housing())) {
            spec = spec.and(findByHousing(filter.housing()));
        }
        if (StringUtils.isNotEmpty(filter.apartment())) {
            spec = spec.and(findByApartment(filter.apartment()));
        }
        return spec;
    }

    private static Specification<Address> findByCountry(String country) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("country"), country);
    }

    private static Specification<Address> findByPostalCode(String postalCode) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("postalCode"), postalCode);
    }

    private static Specification<Address> findByCity(String city) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("city"), city);
    }

    private static Specification<Address> findByStreet(String street) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("street"), street);
    }

    private static Specification<Address> findByHouse(String house) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("house"), house);
    }

    private static Specification<Address> findByHousing(String housing) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("housing"), housing);
    }

    private static Specification<Address> findByApartment(String apartment) {
        return (address, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(address.get("apartment"), apartment);
    }
}