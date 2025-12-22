package org.ikitadevs.dataapi.controller;


import org.ikitadevs.dataapi.dto.response.ProcessResponse;
import org.ikitadevs.dataapi.dto.request.ProcessRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Value("${internal.token}")
    private String internalToken;

    @PostMapping("/api/transform")
    public ResponseEntity<?> processing(@RequestBody ProcessRequest processRequest) {
        return ResponseEntity.ok(new ProcessResponse(processRequest.getText().toUpperCase()));
    }
}
