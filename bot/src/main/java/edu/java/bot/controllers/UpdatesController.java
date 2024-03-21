package edu.java.bot.controllers;

import edu.java.bot.requests.LinkUpdateRequest;
import edu.java.bot.services.UpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdatesController {
    private final UpdateService updateService;

    @PostMapping
    public String handleLinkUpdateRequest(@RequestBody @Valid LinkUpdateRequest request) {
        updateService.add(request);

        return "Update processed successfully";
    }
}
