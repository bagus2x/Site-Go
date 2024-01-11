package id.co.mii.sitego.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    @NotNull
    @Size(min = 4, max = 64)
    private String username;

    @NotNull
    @Size(min = 4, max = 32)
    private String password;
}
