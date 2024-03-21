package edu.java.bot.services;

import edu.java.bot.exceptions.DuplicateUpdateException;
import edu.java.bot.requests.LinkUpdateRequest;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    private final Set<LinkUpdateRequest> updates = new HashSet<>();

    public boolean add(LinkUpdateRequest request) {
        if (updates.contains(request)) {
            throw new DuplicateUpdateException("Update already exists");
        }

        return updates.add(request);
    }
}
