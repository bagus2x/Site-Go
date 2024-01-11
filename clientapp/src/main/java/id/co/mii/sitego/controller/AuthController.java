package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.LoginRequest;
import id.co.mii.sitego.model.request.VerificationRequest;
import id.co.mii.sitego.service.AuthService;
import id.co.mii.sitego.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ProfileService profileService;

    @GetMapping("/login")
    public String loginView() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(LoginRequest request) {
        authService.login(request);

        return "redirect:/dashboard";
    }

    @GetMapping("/verification")
    public String verificationView(@RequestParam(name = "token") String token, Model model) {
        Profile profile = profileService.getByToken(token);
        model.addAttribute(profile);

        return "auth/verification";
    }

    @PostMapping("/verification")
    public String verification(@RequestParam(name = "token") String token, VerificationRequest request) {
        authService.verify(token, request);

        return "redirect:/login?success=true&message=Verified";
    }
}
