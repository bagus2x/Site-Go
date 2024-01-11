package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.UpdateProfileRequest;
import id.co.mii.sitego.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONSULTANT')")
    public List<Profile> getAll(@RequestParam(name = "roles") Set<String> roleNames) {
        Set<String> roles = roleNames.stream().map(roleName -> roleName.replace("'", "")).collect(Collectors.toSet());
        return profileService.getAll(roles);
    }

    @GetMapping("/profile/{profileId}")
    public Profile getById(@PathVariable Integer profileId) {
        return profileService.getById(profileId);
    }

    @GetMapping("/profile/token")
    public Profile getByToken(@RequestParam(name = "token") String token) {
        return profileService.getByToken(token);
    }

    @PutMapping("/profile")
    public Profile update(@RequestBody UpdateProfileRequest request) {
        return profileService.update(request);
    }

    @PutMapping("/profile/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Profile update(@PathVariable Integer profileId, @RequestBody UpdateProfileRequest request) {
        return profileService.update(profileId, request);
    }
}
