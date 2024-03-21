package edu.java.bot.models;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long chatId;
    private List<URI> links;
    private SessionState state;

    public boolean isAwaitingTrackingLink() {
        return state.equals(SessionState.AWAITING_TRACKING_LINK);
    }

    public boolean isAwaitingUnTrackingLink() {
        return state.equals(SessionState.AWAITING_UNTRACKING_LINK);
    }
}
