package org.ikitadevs.dataapi.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ProcessRequest {
    private String text;
    private UUID user_id;
}
