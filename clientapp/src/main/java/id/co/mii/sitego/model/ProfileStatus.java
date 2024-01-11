package id.co.mii.sitego.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileStatus {
    private Integer id;

    private String name;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
