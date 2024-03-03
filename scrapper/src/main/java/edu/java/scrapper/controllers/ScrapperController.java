package edu.java.scrapper.controllers;

import edu.java.scrapper.requests.AddLinkRequest;
import edu.java.scrapper.requests.RemoveLinkRequest;
import edu.java.scrapper.responses.LinkResponse;
import edu.java.scrapper.responses.ListLinksResponse;
import edu.java.scrapper.services.ChatLinkService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("UastIncorrectHttpHeaderInspection")
public class ScrapperController {
    private final ChatLinkService chatLinkService;

    @PostMapping("/tg-chat/{id}")
    public String registerChatById(@PathVariable("id") Long id) {
        chatLinkService.registerChatById(id);

        return "Chat registered";
    }

    @DeleteMapping("/tg-chat/{id}")
    public String deleteChatById(@PathVariable("id") Long id) {
        chatLinkService.deleteChatById(id);

        return "Chat successfully deleted";
    }

    @GetMapping("/links")
    public ListLinksResponse getChatLinksByChatId(@RequestHeader("Tg-Chat-Id") Long chatId) {
        List<LinkResponse> links = chatLinkService.getLinksByChatId(chatId);

        return new ListLinksResponse(links, links.size());
    }

    @PostMapping("/links")
    public LinkResponse addChatLinkByChatId(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid @NotNull AddLinkRequest request
    ) {
        return chatLinkService.addLinkByChatId(chatId, request.link());
    }

    @DeleteMapping("/links")
    public LinkResponse deleteChatLinkByChatId(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid @NotNull RemoveLinkRequest request
    ) {
        return chatLinkService.deleteLinkByChatId(chatId, request.link());
    }
}
