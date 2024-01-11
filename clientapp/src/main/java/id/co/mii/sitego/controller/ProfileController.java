package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.service.ProfileStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import id.co.mii.sitego.model.request.UpdateProfileRequest;
import id.co.mii.sitego.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileStatusService profileStatusService;

    @GetMapping("/profile")
    public String profileView() {
        return "profile/profile";
    }

    @GetMapping("/profile/settings")
    public String settingsView(Model model) {
        List<ProfileStatus> profileStatuses = profileStatusService.getAll();
        model.addAttribute("profileStatuses", profileStatuses);

        return "profile/settings";
    }

    @GetMapping("/profile/{profileId}")
    public String profileView(@PathVariable Integer profileId, Model model) {
        Profile profile = profileService.getById(profileId);
        model.addAttribute("profile", profile);

        return "profile/detail";
    }

    @PutMapping("/profile")
    public String update(UpdateProfileRequest request) {
        profileService.update(request);

        return "redirect:/profile";
    }
}
