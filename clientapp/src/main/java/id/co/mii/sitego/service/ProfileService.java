package id.co.mii.sitego.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final RestTemplate restTemplate;
    private final Cloudinary cloudinary;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public List<Profile> getAll(Set<String> roleNames) {
        String params = String.join(",", roleNames);
        Profile[] profiles = restTemplate.getForEntity(baseUrl + "/profiles?roles=" + params, Profile[].class).getBody();
        return Arrays.asList(Objects.requireNonNull(profiles));
    }

    public Profile getById(Integer profileId) {
        return restTemplate.getForObject(baseUrl + "/profile/" + profileId, Profile.class);
    }

    public Profile getByToken(String token) {
        return restTemplate.getForObject(baseUrl + "/profile/token?token=" + token, Profile.class);
    }

    // Update by id. The admin uses this method
    public Profile update(Integer profileId, UpdateProfileRequest request) {
        Profile profile = getById(profileId);
        return update(baseUrl + "/profile/" + profileId, profile, request);
    }

    // Update by authenticated profile
    public Profile update(UpdateProfileRequest request) {
        AbstractAuthenticationToken authentication = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Profile profile = (Profile) authentication.getDetails();
        profile = update(baseUrl + "/profile", profile, request);
        authentication.setDetails(profile);
        return profile;
    }

    private Profile update(String url, Profile profile, UpdateProfileRequest request) {
        try {
            CompletableFuture<String> photoFuture = uploadFile(request.getPhoto());
            CompletableFuture<String> cvFuture = uploadFile(request.getCv());

            CompletableFuture.allOf(photoFuture, cvFuture).join();

            String newPhoto = photoFuture.get();
            String newCv = cvFuture.get();

            String previousPhoto = profile.getPhoto();
            String previousCv = profile.getCv();

            HashMap<String, Object> payload = new HashMap<String, Object>() {
                {
                    put("name", request.getName());
                    put("username", request.getUsername());
                    put("gender", request.getGender());
                    put("status", request.getStatus());

                    if (newPhoto != null) put("photo", newPhoto);
                    else put("photo", previousPhoto);

                    if (newCv != null) put("cv", newCv);
                    else put("cv", previousCv);

                    if (request.getRoles() != null) put("roles", request.getRoles());

                    put("phone", request.getPhone());
                }
            };

            return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(payload),
                Profile.class
            ).getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    protected CompletableFuture<String> uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.getBytes().length == 0) {
            return CompletableFuture.completedFuture(null);
        }

        Map<?, ?> options = ObjectUtils.asMap("resource_type", "auto");
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);
        return CompletableFuture.completedFuture((String) result.get("secure_url"));
    }
}
