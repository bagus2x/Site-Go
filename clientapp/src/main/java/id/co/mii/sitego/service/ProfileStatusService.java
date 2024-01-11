package id.co.mii.sitego.service;

import id.co.mii.sitego.model.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProfileStatusService {
    private final RestTemplate restTemplate;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public List<ProfileStatus> getAll() {
        ProfileStatus[] statuses = restTemplate.getForEntity(baseUrl + "/profile-statuses", ProfileStatus[].class).getBody();
        return Arrays.asList(Objects.requireNonNull(statuses));
    }
}
