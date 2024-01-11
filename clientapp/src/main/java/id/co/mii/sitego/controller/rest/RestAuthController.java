package id.co.mii.sitego.controller.rest;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.RegistrationRequest;
import id.co.mii.sitego.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RestAuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    public Profile register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }
}
