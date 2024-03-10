package edu.java.scrapper.configs;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static edu.java.scrapper.IntegrationTest.POSTGRES;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
    }
}
