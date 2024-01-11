package id.co.mii.sitego.controller.rest;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.model.request.UpdateProfileRequest;
import id.co.mii.sitego.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RestProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles")
    public List<Profile> getAll(@RequestParam(defaultValue = "ADMIN,CONSULTANT,EMPLOYEE", name = "roles") Set<String> roleNames) {
        return profileService.getAll(roleNames);
    }

    @GetMapping("/profile/{profileId}")
    public Profile getById(@PathVariable Integer profileId) {
        return profileService.getById(profileId);
    }

    @PutMapping("/profile/{profileId}")
    public Profile update(@PathVariable Integer profileId, @ModelAttribute UpdateProfileRequest request) {
        return profileService.update(profileId, request);
    }
}
