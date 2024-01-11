package id.co.mii.sitego.controller.rest;

import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.service.ProfileStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestProfileStatusRepository {
    private final ProfileStatusService profileStatusService;

    @GetMapping("/profile-statuses")
    public List<ProfileStatus> getAll() {
        return profileStatusService.getAll();
    }
}
