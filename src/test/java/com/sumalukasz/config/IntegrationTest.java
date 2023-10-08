package com.sumalukasz.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationTest {

    private final static String profile;
    static final MySQLContainer DATABASE = new MySQLContainer(DockerImageName.parse("mysql:8.1.0"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("password");

    static {
        profile = System.getProperty("spring.profiles.active", "test");

        if ("test".equals(profile)) {
            DATABASE.start();
        }
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry propertyRegistry) {
        if ("test".equals(profile)) {
            propertyRegistry.add("spring.datasource.url", DATABASE::getJdbcUrl);
            propertyRegistry.add("spring.datasource.username", DATABASE::getUsername);
            propertyRegistry.add("spring.datasource.password", DATABASE::getPassword);
        }
    }

}
