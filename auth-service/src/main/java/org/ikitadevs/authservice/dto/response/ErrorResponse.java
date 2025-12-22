package org.ikitadevs.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
}
