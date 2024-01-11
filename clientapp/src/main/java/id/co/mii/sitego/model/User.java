package id.co.mii.sitego.model;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String token;

    private Boolean isEnabled;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private List<Role> roles;
}
