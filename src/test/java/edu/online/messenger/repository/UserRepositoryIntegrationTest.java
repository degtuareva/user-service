package edu.online.messenger.repository;

import edu.online.messenger.config.AbstractIntegrationTest;
import edu.online.messenger.constant.RoleName;
import edu.online.messenger.model.entity.User;
import edu.online.messenger.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnOnlyUsersWithLastVisitBeforeThreshold() {
        LocalDateTime threshold = LocalDateTime.of(2025, 6, 5, 0, 0);

        List<User> usersBeforeThreshold = userRepository.findByLastVisitDateBefore(threshold);

        assertThat(usersBeforeThreshold)
                .extracting(User::getLogin)
                .contains("oldUser")
                .doesNotContain("newUser");
    }

    @Test
    void shouldReturnTrueWhenLoginExists() {
        assertThat(userRepository.existsByLogin("oldUser")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenLoginDoesNotExist() {
        assertThat(userRepository.existsByLogin("nonexistent")).isFalse();
    }

    @Test
    void shouldReturnUserWhenLoginExists() {
        Optional<User> found = userRepository.findByLogin("oldUser");
        Optional<User> notFound = userRepository.findByLogin("nonexistent");

        assertThat(found).isPresent();
        assertThat(found.get().getLogin()).isEqualTo("oldUser");
        assertThat(notFound).isEmpty();
    }

    @Test
    void shouldSaveUserAndFindById() {
        User user = UserTestBuilder.builder()
                .withLogin("integrationTestUser")
                .withPassword("password")
                .withRole(RoleName.USER)
                .withLastVisitDate(LocalDateTime.now().minusDays(1))
                .build()
                .buildUser();

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getLogin()).isEqualTo("integrationTestUser");
    }
}