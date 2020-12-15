package com.github.dhodja92.springdatajpademo.testcontainers;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@TestConfiguration
public class PostgreSqlContainerConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgreSqlContainer() {
        PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:12.3");
        postgreSqlContainer.start();
        try (DatabaseDelegate delegate = new JdbcDatabaseDelegate(postgreSqlContainer, "")) {
            delegate.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"", "", 0, false, false);
        }
        return postgreSqlContainer;
    }

    @Bean
    public HikariDataSource dataSource(PostgreSQLContainer<?> postgreSqlContainer) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgreSqlContainer.getJdbcUrl());
        dataSource.setUsername(postgreSqlContainer.getUsername());
        dataSource.setPassword(postgreSqlContainer.getPassword());
        return dataSource;
    }
}
