package edu.online.messenger.repository;

import edu.online.messenger.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByLastVisitDateBefore(LocalDateTime threshold);

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);
}