package edu.online.messenger.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersHolder {

    public static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:17.4-alpine")
                    .withDatabaseName("edu_online_messenger_test")
                    .withUsername("test")
                    .withPassword("test");
}