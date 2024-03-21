package edu.java.bot.validators;

import java.net.URI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class LinkValidator {
    private LinkValidator nextValidator;

    @Contract("_, _ -> param1")
    public static LinkValidator chainValidators(LinkValidator first, LinkValidator @NotNull... validators) {
        LinkValidator headValidator = first;

        for (LinkValidator nextValidator : validators) {
            headValidator.nextValidator = nextValidator;
            headValidator = nextValidator;
        }

        return first;
    }

    public final boolean isLinkValid(@NotNull URI uri) {
        if (uri.getHost().equals(getHostName())) {
            return true;
        }

        if (nextValidator != null) {
            return nextValidator.isLinkValid(uri);
        }

        return false;
    }

    protected abstract String getHostName();
}
