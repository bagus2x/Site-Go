package id.co.mii.sitego.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckConflictScheduleRequest {
    @NonNull
    @Positive
    private Integer consultantId;

    @NotNull
    private Long time;

    @NonNull
    @Positive
    private Integer duration;
}
