package id.co.mii.sitego.service;

import id.co.mii.sitego.model.*;
import id.co.mii.sitego.model.request.LoginRequest;
import id.co.mii.sitego.model.request.RegistrationRequest;
import id.co.mii.sitego.model.request.VerificationRequest;
import id.co.mii.sitego.repository.ProfileRepository;
import id.co.mii.sitego.repository.ProfileStatusRepository;
import id.co.mii.sitego.repository.RoleRepository;
import id.co.mii.sitego.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final Validator validator;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileStatusRepository profileStatusRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Profile register(RegistrationRequest request) {
        Set<ConstraintViolation<RegistrationRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email has already been taken");
        }

        Role role = roleRepository.findByName("EMPLOYEE")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setIsEnabled(false);
        user.setRoles(Collections.singleton(role));
        user.setToken(UUID.randomUUID().toString());

        ProfileStatus status = profileStatusRepository.findByName("IDLE")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile status is not found"));

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setUser(user);
        profile.setStatus(status);
        user.setProfile(profile);

        profile = profileRepository.save(profile);

        return profile;
    }

    public Profile login(LoginRequest request) {
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authentication = authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUser().getProfile();
    }

    public Profile verify(String token, VerificationRequest request) {
        Set<ConstraintViolation<VerificationRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        User user = userRepository.findByToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not found"));

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username has already been taken");
        }

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getProfile().setPhone(request.getPhone());
        user.setIsEnabled(true);
        user.setToken(null);

        user = userRepository.save(user);

        return user.getProfile();
    }
}
