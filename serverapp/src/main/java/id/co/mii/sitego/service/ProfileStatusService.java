package id.co.mii.sitego.service;

import id.co.mii.sitego.model.ProfileStatus;
import id.co.mii.sitego.model.request.ProfileStatusRequest;
import id.co.mii.sitego.repository.ProfileStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProfileStatusService {
    private final ProfileStatusRepository profileStatusRepository;
    private final Validator validator;

    public ProfileStatus create(ProfileStatusRequest request) {
        Set<ConstraintViolation<ProfileStatusRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (profileStatusRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Profile status name already exists");
        }

        ProfileStatus status = new ProfileStatus();
        status.setName(request.getName());

        return profileStatusRepository.save(status);
    }

    public List<ProfileStatus> getAll() {
        return profileStatusRepository.findAll();
    }

    public ProfileStatus getById(Integer statusId) {
        return profileStatusRepository.findById(statusId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile status is not found"));
    }

    public ProfileStatus update(Integer statusId, ProfileStatusRequest request) {
        Set<ConstraintViolation<ProfileStatusRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        ProfileStatus status = getById(statusId);
        status.setUpdatedAt(LocalDateTime.now());
        status.setName(request.getName());

        return profileStatusRepository.save(status);
    }

    public ProfileStatus delete(Integer statusId) {
        ProfileStatus status = getById(statusId);
        profileStatusRepository.delete(status);

        return status;
    }
}
