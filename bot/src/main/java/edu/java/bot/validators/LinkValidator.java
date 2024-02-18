package edu.java.bot.validators;

import java.net.URI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class LinkValidator {
    private LinkValidator next;

    @Contract("_, _ -> param1")
    public static LinkValidator link(LinkValidator first, LinkValidator @NotNull... validators) {
        LinkValidator head = first;

        for (LinkValidator nextValidator : validators) {
            head.next = nextValidator;
            head = nextValidator;
        }

        return first;
    }

    public final boolean isValid(@NotNull URI uri) {
        if (uri.getHost().equals(getHostName())) {
            return true;
        }

        if (next != null) {
            return next.isValid(uri);
        }

        return false;
    }

    protected abstract String getHostName();
}
