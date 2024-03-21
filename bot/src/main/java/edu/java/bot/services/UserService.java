package edu.java.bot.services;

import edu.java.bot.models.SessionState;
import edu.java.bot.models.User;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public void clear() {
        users.clear();
    }

    public void addUser(User user) {
        users.put(user.getChatId(), user);
    }

    public Optional<User> findById(long chatId) {
        return users.containsKey(chatId)
            ? Optional.of(users.get(chatId))
            : Optional.empty();
    }

    public boolean register(long chatId) {
        Optional<User> initiator = findById(chatId);

        if (initiator.isEmpty()) {
            addUser(new User(chatId, List.of(), SessionState.DEFAULT));

            return true;
        }

        return false;
    }

    public boolean changeSessionState(long chatId, SessionState newState) {
        Optional<User> initiator = findById(chatId);

        if (initiator.isPresent()) {
            User user = initiator.get();
            user.setState(newState);
            addUser(user);

            return true;
        }

        return false;
    }

    public boolean addLink(@NotNull User user, URI uri) {
        List<URI> links = new ArrayList<>(user.getLinks());

        if (links.contains(uri)) {
            return false;
        }

        links.add(uri);
        updateLinks(user, links);

        return true;
    }

    public boolean deleteLink(@NotNull User user, URI uri) {
        List<URI> links = new ArrayList<>(user.getLinks());

        if (!links.contains(uri)) {
            return false;
        }

        links.remove(uri);
        updateLinks(user, links);

        return true;
    }

    private void updateLinks(@NotNull User user, List<URI> links) {
        user.setLinks(links);
        user.setState(SessionState.DEFAULT);

        addUser(user);
    }
}
