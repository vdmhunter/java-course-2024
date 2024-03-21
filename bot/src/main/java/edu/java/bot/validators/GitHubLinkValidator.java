package edu.java.bot.validators;

public class GitHubLinkValidator extends LinkValidator {
    @Override
    protected String getHostName() {
        return "github.com";
    }
}
