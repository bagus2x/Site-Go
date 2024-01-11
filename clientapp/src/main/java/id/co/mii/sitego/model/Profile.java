package id.co.mii.sitego.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Profile {
    private Integer id;

    private String gender;

    private String name;

    private String cv;

    private String photo;

    private String phone;

    private User user;

    private ProfileStatus status;
}
