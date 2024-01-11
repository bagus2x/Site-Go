package id.co.mii.sitego.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckConflictScheduleRequest {
    private Integer consultantId;

    private Long time;

    private Integer duration;
}
