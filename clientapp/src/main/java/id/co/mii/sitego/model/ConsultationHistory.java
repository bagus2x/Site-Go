package id.co.mii.sitego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsultationHistory {
    private Id id;

    private String description;

    private LocalDateTime createdAt;

    private Profile editor;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Id implements Serializable {
        private static final long serialVersionUID = -1674575896565639073L;

        private Consultation consultation;

        private ConsultationStatus consultationStatus;
    }
}
