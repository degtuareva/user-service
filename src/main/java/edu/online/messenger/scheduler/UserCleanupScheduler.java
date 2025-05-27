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
        LocalDateTime threshold = LocalDateTime.now().minusYears(3);
        List<User> usersToDelete = userRepository.findByLastVisitDateBefore(threshold);
        int count = usersToDelete.size();
        if (count > 0) {
            userRepository.deleteByLastVisitDateBefore(threshold);
            log.info("Удалено {} пользователей с lastVisitDate до {}", count, threshold);
        } else {
            log.info("Нет пользователей для удаления по состоянию на {}", threshold);
        }
    }
}