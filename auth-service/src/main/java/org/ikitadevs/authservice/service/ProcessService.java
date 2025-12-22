package org.ikitadevs.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.authservice.dto.request.ProcessRequest;
import org.ikitadevs.authservice.dto.response.ProcessResponse;
import org.ikitadevs.authservice.model.ProcessLog;
import org.ikitadevs.authservice.repository.ProcessLogsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessService {
    private final RestTemplate restTemplate;
    private final ProcessLogsRepository processLogsRepository;

    @Value("${services.data-api}")
    String baseUrl;

    @Value("${internal.token}")
    private String internalToken;

    @Transactional
    public String callDataApi(ProcessRequest processRequest) {
        log.info(processRequest.toString());
        String url = baseUrl + "/api/transform";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);

        HttpEntity<ProcessRequest> entity = new HttpEntity<>(processRequest, headers);
        try {
            ProcessResponse response = restTemplate.postForObject(url, entity, ProcessResponse.class);
            return response.getText();
        } catch (HttpStatusCodeException e) {
            log.error("API error: Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
           throw new ResponseStatusException(e.getStatusCode(), "Data API error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Connection failed", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Data API is unreachable");
        }
    }

    @Transactional
    public void createAndSaveLogs(UUID id, String input_text, String output_text) {
        ProcessLog processLog = new ProcessLog();
        processLog.setUser_id(id);
        processLog.setOutput_text(output_text);
        processLog.setInput_text(input_text);
        processLog.setCreated_at(Instant.now());
        processLogsRepository.save(processLog);
    }

}
