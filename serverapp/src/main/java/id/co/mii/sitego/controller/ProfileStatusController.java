package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.model.request.ProfileStatusRequest;
import id.co.mii.sitego.service.ProfileStatusService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProfileStatusController {
    private final ProfileStatusService profileStatusService;

    @PostMapping("/profile-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ProfileStatus create(@RequestBody ProfileStatusRequest request) {
        return profileStatusService.create(request);
    }

    @GetMapping("/profile-statuses")
    public List<ProfileStatus> getAll() {
        return profileStatusService.getAll();
    }

    @GetMapping("/profile-status/{statusId}")
    public ProfileStatus getById(@PathVariable Integer statusId) {
        return profileStatusService.getById(statusId);
    }

    @PutMapping("/profile-status/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProfileStatus update(@PathVariable Integer statusId, ProfileStatusRequest request) {
        return profileStatusService.update(statusId, request);
    }

    @DeleteMapping("/profile-status/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProfileStatus update(@PathVariable Integer statusId) {
        return profileStatusService.delete(statusId);
    }
}
