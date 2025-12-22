package org.ikitadevs.authservice.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "processing_log")
@Entity
public class ProcessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private UUID user_id;

    private String input_text;

    private String output_text;
    private Instant created_at;
}
