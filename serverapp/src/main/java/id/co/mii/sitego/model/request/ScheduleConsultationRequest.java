package id.co.mii.sitego.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleConsultationRequest {
    @Positive
    @NotNull
    private Integer consultantId;

    @NotBlank
    private String venue;

    @Positive
    @NotNull
    private Long time;

    @Positive
    @NotNull
    private Long duration;

    private String description;
}
