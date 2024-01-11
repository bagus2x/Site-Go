package id.co.mii.sitego.service;

import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.request.LoginRequest;
import id.co.mii.sitego.model.request.RegistrationRequest;
import id.co.mii.sitego.model.request.VerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public Profile login(LoginRequest request) {
        Profile profile = restTemplate.postForObject(baseUrl + "/login", request, Profile.class);
        setPrinciple(Objects.requireNonNull(profile), request.getPassword());

        return profile;
    }

    public void setPrinciple(Profile profile, String password) {
        List<SimpleGrantedAuthority> authorities = profile.getUser()
            .getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            profile.getUser().getUsername(),
            password,
            authorities
        );
        token.setDetails(profile);

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public Profile register(RegistrationRequest request) {
        Profile profile = restTemplate.postForObject(baseUrl + "/registration", request, Profile.class);

        sendEmailVerification(Objects.requireNonNull(profile));

        return profile;
    }

    private void sendEmailVerification(Profile profile) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(profile.getUser().getEmail());
            helper.setSubject(String.format("Hi %s Please Verify Your Account!!", profile.getName()));

            Context context = new Context();
            context.setVariable("profile", profile);

            String html = templateEngine.process("auth/email-verification", context);

            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile verify(String token, VerificationRequest request) {
        return restTemplate.postForObject(baseUrl + "/verification?token=" + token, request, Profile.class);
    }
}
