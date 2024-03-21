package edu.java.scrapper.services;

import edu.java.scrapper.exceptions.BadRequestException;
import edu.java.scrapper.exceptions.ResourceNotFoundException;
import edu.java.scrapper.responses.LinkResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatLinkService {
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();

    public void registerChatById(long chatId) {
        if (chatLinks.containsKey(chatId)) {
            throw new BadRequestException("Chat already registered", "Cannot register a chat again");
        }

        chatLinks.put(chatId, new ArrayList<>());
    }

    public void deleteChatById(Long chatId) {
        validateChatRegistration(chatId, "Cannot delete an unregistered chat");
        chatLinks.remove(chatId);
    }

    public List<LinkResponse> getLinksByChatId(Long chatId) {
        validateChatRegistration(chatId, "Cannot retrieve links for an unregistered chat");

        return chatLinks.get(chatId);
    }

    public LinkResponse addLinkByChatId(Long chatId, URI link) {
        validateChatRegistration(chatId, "Cannot add a link for an unregistered chat");
        List<LinkResponse> linkResponses = chatLinks.get(chatId);

        boolean isLinkTracked = linkResponses.stream()
            .anyMatch(response -> response.url().getPath().equals(link.getPath()));

        if (isLinkTracked) {
            throw new BadRequestException("Link already tracked", "Cannot add an already tracked link");
        }

        LinkResponse linkResponse = new LinkResponse((long) (linkResponses.size() + 1), link);
        linkResponses.add(linkResponse);

        return linkResponse;
    }

    public LinkResponse deleteLinkByChatId(Long chatId, URI link) {
        validateChatRegistration(chatId, "Cannot delete a link for an unregistered chat");
        List<LinkResponse> linkResponses = chatLinks.get(chatId);

        LinkResponse linkResponse = linkResponses.stream()
            .filter(lr -> lr.url().getPath().equals(link.getPath()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Link not found", "Cannot delete an unfounded link"));

        linkResponses.remove(linkResponse);

        return linkResponse;
    }

    private void validateChatRegistration(Long chatId, String message) {
        if (!chatLinks.containsKey(chatId)) {
            throw new ResourceNotFoundException("Chat was not registered", message);
        }
    }
}
