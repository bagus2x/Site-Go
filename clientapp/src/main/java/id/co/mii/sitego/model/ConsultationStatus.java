package id.co.mii.sitego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsultationStatus {
    private Integer id;

    private String name;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
