package id.co.mii.sitego.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import id.co.mii.sitego.model.Gender;
import id.co.mii.sitego.model.Profile;
import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.model.Role;
import id.co.mii.sitego.model.UserDetails;
import id.co.mii.sitego.model.request.UpdateProfileRequest;
import id.co.mii.sitego.repository.ProfileRepository;
import id.co.mii.sitego.repository.ProfileStatusRepository;
import id.co.mii.sitego.repository.RoleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final ProfileStatusRepository profileStatusRepository;

    private final RoleRepository roleRepository;


    private final Validator validator;

    public List<Profile> getAll(Set<String> roleNames) {
        return profileRepository.findDistinctByUserRolesNameIn(roleNames);
    }

    public Profile update(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Profile profile = userDetails.getUser().getProfile();

        return update(profile, request);
    }

    public Profile update(Integer profileId, UpdateProfileRequest request) {
        Profile profile = getById(profileId);

        return update(profile, request);
    }

    private Profile update(Profile profile, UpdateProfileRequest request) {
        Set<ConstraintViolation<UpdateProfileRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        profile.setName(request.getName());
        profile.setGender(Gender.valueOf(request.getGender()));
        profile.setPhoto(request.getPhoto());
        profile.setPhone(request.getPhone());
        profile.setCv(request.getCv());

        if (request.getStatus() != null) {
            ProfileStatus status = profileStatusRepository.findByName(request.getStatus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status is not found"));
            profile.setStatus(status);
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = request.getRoles()
                .stream()
                .map((role) -> roleRepository.findByName(role)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found"))
                )
                .collect(Collectors.toSet());
            profile.getUser().setRoles(roles);
        }

        return profileRepository.save(profile);
    }

    public Profile getById(Integer profileId) {
        return profileRepository.findById(profileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile is not found"));
    }

    public Profile getByToken(String token) {
        return profileRepository.findByUserToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not found"));
    }
}
