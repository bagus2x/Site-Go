package id.co.mii.sitego.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleConsultationRequest {
    private Integer consultantId;

    private String venue;

    private Long time;

    private  Long duration;

    private String description;
}
