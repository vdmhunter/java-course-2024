package edu.java.bot.configs;

import edu.java.bot.validators.GitHubLinkValidator;
import edu.java.bot.validators.LinkValidator;
import edu.java.bot.validators.StackOverflowLinkValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkValidatorConfig {
    @Bean
    public LinkValidator linkValidator() {
        return LinkValidator.link(
            new GitHubLinkValidator(),
            new StackOverflowLinkValidator()
        );
    }
}
