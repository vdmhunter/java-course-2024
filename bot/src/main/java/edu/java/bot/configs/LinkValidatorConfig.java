package edu.java.bot.configs;

import edu.java.bot.validators.GitHubLinkValidator;
import edu.java.bot.validators.LinkValidator;
import edu.java.bot.validators.StackOverflowLinkValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkValidatorConfig {
    @Bean
    public GitHubLinkValidator getGitHubLinkValidator() {
        return new GitHubLinkValidator();
    }

    @Bean
    public StackOverflowLinkValidator getStackOverflowLinkValidator() {
        return new StackOverflowLinkValidator();
    }

    @Bean
    public LinkValidator linkValidator(
        GitHubLinkValidator gitHubLinkValidator,
        StackOverflowLinkValidator stackOverflowLinkValidator
    ) {
        return LinkValidator.chainValidators(gitHubLinkValidator, stackOverflowLinkValidator);
    }
}
