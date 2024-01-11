package id.co.mii.sitego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report {
    private Integer id;

    private String description;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private Consultation consultation;
}
