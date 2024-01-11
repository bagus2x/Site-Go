package id.co.mii.sitego.model.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProfileRequest {
    @NotNull
    @Size(min = 3, max = 127)
    private String name;

    @NotNull
    @Size(min = 3, max = 8)
    private String gender;

    @Size(min = 3, max = 8)
    private String status;

    private String photo;

    @Size(min = 3, max = 16)
    private String phone;

    private String cv;

    private Set<@NotBlank String> roles;
}
