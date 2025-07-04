package edu.online.messenger.repository;

import edu.online.messenger.config.AbstractIntegrationTest;
import edu.online.messenger.model.entity.Address;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.util.AddressTestBuilder;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/setup.sql")
public class AddressRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private AddressRepository addressRepository;

    private final User testUser = UserTestBuilder.builder().withId(5L).build().buildUser();
    private final Address testAddress = AddressTestBuilder.builder().withId(10L).build().buildAddress();

    @Test
    void findByUserIdShouldReturnListOfAddresses() {
        List<Address> addresses = addressRepository.findByUserId(testUser.getId());

        assertThat(addresses).hasSize(1);
        assertThat(addresses.get(0).getId()).isEqualTo(testAddress.getId());
        assertThat(addresses.get(0).getUser().getId()).isEqualTo(testUser.getId());
        assertThat(addresses.get(0).getCity()).isEqualTo("testCity");
        assertThat(addresses.get(0).getStreet()).isEqualTo("testStreet");
        assertThat(addresses.get(0).getCountry()).isEqualTo("testCountry");
        assertThat(addresses.get(0).getPostalCode()).isEqualTo("testPostalCode");
        assertThat(addresses.get(0).getHouse()).isEqualTo(1);
        assertThat(addresses.get(0).getHousing()).isEqualTo("testHousing");
        assertThat(addresses.get(0).getApartment()).isEqualTo(1);
    }

    @Test
    void findByUserIdShouldReturnEmptyListWhenUserHaveNoAddresses() {
        addressRepository.deleteAll();

        List<Address> addresses = addressRepository.findByUserId(testUser.getId());

        assertThat(addresses).isEmpty();
    }
}