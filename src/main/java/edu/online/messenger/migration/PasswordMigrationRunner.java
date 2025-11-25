package edu.online.messenger.migration;

import edu.online.messenger.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordMigrationRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordMigrationRunner(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findAll().forEach(user -> {
            String pwd = user.getPassword();
            // Если пароль не bcrypt
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                user.setPassword(passwordEncoder.encode(pwd));
                System.out.println("Захеширован пароль для пользователя: " + user.getLogin());
            }
        });
        userRepository.saveAll(userRepository.findAll());
        System.out.println("Миграция паролей завершена.");
    }
}