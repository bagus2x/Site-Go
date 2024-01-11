package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.LoginRequest;
import id.co.mii.sitego.model.request.RegistrationRequest;
import id.co.mii.sitego.model.request.VerificationRequest;
import id.co.mii.sitego.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    public Profile register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Profile login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/verification")
    public Profile verify(@RequestParam(name = "token") String token, @RequestBody VerificationRequest request) {
        return authService.verify(token, request);
    }
}
