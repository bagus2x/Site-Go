package id.co.mii.sitego.model.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProfileRequest {
    private String name;

    private String username;

    private String gender;

    private String status;

    private MultipartFile photo;

    private String phone;

    private MultipartFile cv;

    private Set<String> roles;
}
