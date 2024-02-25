package edu.java.scrapper.configurations.clients;

import edu.java.scrapper.clients.github.RegularWebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubConfig {
    @Bean
    @Qualifier("github")
    public RegularWebClient regularWebClient() {
        return new RegularWebClient();
    }
}
