package id.co.mii.sitego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
    private Integer id;

    private String name;

    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime createdAt = LocalDateTime.now();
}
