package org.ikitadevs.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.ikitadevs.authservice.dto.request.ProcessRequest;
import org.ikitadevs.authservice.model.ProcessLog;
import org.ikitadevs.authservice.model.User;
import org.ikitadevs.authservice.service.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.awt.SystemColor.text;

@RestController
@RequiredArgsConstructor
public class ProcessController {
    private final ProcessService processService;

    @PostMapping("/api/process")
    public ResponseEntity<?> sendText(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        String inputText = request.get("text");
        if(inputText == null || inputText.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Text is required!"));
        }
        ProcessRequest processRequest = new ProcessRequest();
        processRequest.setText(inputText);
        request.clear();
        processRequest.setUser_id(user.getId());
        String outputText = processService.callDataApi(processRequest);

        processService.createAndSaveLogs(user.getId(), inputText, outputText);
        return ResponseEntity.ok(Map.of("result", outputText));
    }




}
