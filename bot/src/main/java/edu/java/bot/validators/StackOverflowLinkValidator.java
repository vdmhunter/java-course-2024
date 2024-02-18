package edu.java.bot.validators;

public class StackOverflowLinkValidator extends LinkValidator {
    @Override
    protected String getHostName() {
        return "stackoverflow.com";
    }
}
