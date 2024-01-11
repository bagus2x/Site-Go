package id.co.mii.sitego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Consultation {
    private Integer id;
    private String venue;

    private LocalDateTime time;

    private Long duration;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private Profile counselee;

    private Profile consultant;

    private ConsultationStatus status;

    private List<ConsultationHistory> histories;
}
