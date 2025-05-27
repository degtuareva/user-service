package edu.online.messenger.scheduler;

import edu.online.messenger.model.entity.User;
import edu.online.messenger.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class UserCleanupScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void deleteInactiveUsers() {
        List<User> usersToDelete = userRepository.findByLastVisitDateBefore(LocalDateTime.now().minusYears(3));
        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
            log.info("Удалено {} пользователей", usersToDelete.size());
        } else {
            log.info("Нет пользователей для удаления");
        }
    }
}