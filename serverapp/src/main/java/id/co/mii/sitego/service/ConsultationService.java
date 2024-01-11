package id.co.mii.sitego.service;

import id.co.mii.sitego.model.*;
import id.co.mii.sitego.model.request.CheckConflictScheduleRequest;
import id.co.mii.sitego.model.request.FinishConsultationRequest;
import id.co.mii.sitego.model.request.RejectConsultationRequest;
import id.co.mii.sitego.model.request.ScheduleConsultationRequest;
import id.co.mii.sitego.repository.ConsultationHistoryRepository;
import id.co.mii.sitego.repository.ConsultationRepository;
import id.co.mii.sitego.repository.ConsultationStatusRepository;
import id.co.mii.sitego.repository.ProfileRepository;
import id.co.mii.sitego.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ConsultationStatusRepository consultationStatusRepository;
    private final ConsultationHistoryRepository consultationHistoryRepository;
    private final ReportRepository reportRepository;
    private final ProfileRepository profileRepository;
    private final Validator validator;

    @Transactional
    public Consultation request() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile counselee = userDetails.getUser().getProfile();

        ConsultationStatus status = consultationStatusRepository.findByName("WAITING")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation status is not found"));

        Consultation consultation = new Consultation();
        consultation.setCounselee(counselee);
        consultation.setStatus(status);

        consultation = consultationRepository.save(consultation);

        ConsultationHistory history = new ConsultationHistory();
        history.setEditor(counselee);
        history.setId(new ConsultationHistory.Id(consultation, status));

        history = consultationHistoryRepository.save(history);

        consultation.setHistories(Collections.singletonList(history)); // Prevent null after insert

        return consultation;
    }

    public List<Consultation> getAll() {
        return consultationRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Consultation> getAllByProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile counselee = userDetails.getUser().getProfile();

        return consultationRepository.findAllByCounseleeIdOrderByCreatedAtDesc(counselee.getId());
    }

    public Consultation getById(Integer consultationId) {
        return consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation is not found"));
    }

    @Transactional
    public Consultation validate(Integer consultationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile admin = userDetails.getUser().getProfile();

        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation is not found"));

        if (!consultation.getStatus().getName().equals("WAITING")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't validate consultation request after " + consultation.getStatus().getName());
        }

        ConsultationStatus status = consultationStatusRepository.findByName("VALIDATED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation status is not found"));
        consultation.setStatus(status);

        ConsultationHistory history = new ConsultationHistory();
        history.setId(new ConsultationHistory.Id(consultation, status));
        history.setEditor(admin);

        consultationHistoryRepository.save(history);

        return consultationRepository.save(consultation);
    }

    @Transactional
    public Consultation reject(Integer consultationId, RejectConsultationRequest request) {
        Set<ConstraintViolation<RejectConsultationRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile admin = userDetails.getUser().getProfile();

        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation is not found"));

        if (!consultation.getStatus().getName().equals("WAITING")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't reject consultation request after " + consultation.getStatus().getName());
        }

        ConsultationStatus status = consultationStatusRepository.findByName("REJECTED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation status is not found"));
        consultation.setStatus(status);

        ConsultationHistory history = new ConsultationHistory();
        history.setId(new ConsultationHistory.Id(consultation, status));
        history.setDescription(request.getDescription());
        history.setEditor(admin);

        consultationHistoryRepository.save(history);

        return consultationRepository.save(consultation);
    }

    @Transactional
    public Consultation schedule(Integer consultationId, ScheduleConsultationRequest request) {
        Set<ConstraintViolation<ScheduleConsultationRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile admin = userDetails.getUser().getProfile();

        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation is not found"));

        if (!consultation.getStatus().getName().equals("VALIDATED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't set schedule consultation request after " + consultation.getStatus().getName());
        }

        Profile consultant = profileRepository.findById(request.getConsultantId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultant is not found"));
        ConsultationStatus status = consultationStatusRepository.findByName("SCHEDULED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation status is not found"));
        consultation.setStatus(status);
        consultation.setConsultant(consultant);
        consultation.setVenue(request.getVenue());
        consultation.setDuration(request.getDuration());

        LocalDateTime time = Instant.ofEpochMilli(request.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        consultation.setTime(time);

        ConsultationHistory history = new ConsultationHistory();
        history.setId(new ConsultationHistory.Id(consultation, status));
        history.setEditor(admin);
        history.setDescription(request.getDescription());

        consultationHistoryRepository.save(history);

        return consultationRepository.save(consultation);
    }

    @Transactional
    public Consultation finish(Integer consultationId, FinishConsultationRequest request) {
        Set<ConstraintViolation<FinishConsultationRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        Profile admin = userDetails.getUser().getProfile();

        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation is not found"));

        if (!consultation.getStatus().getName().equals("SCHEDULED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't finish consultation request after " + consultation.getStatus().getName());
        }

        ConsultationStatus status = consultationStatusRepository.findByName("DONE")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation status is not found"));
        consultation.setStatus(status);

        ConsultationHistory history = new ConsultationHistory();
        history.setId(new ConsultationHistory.Id(consultation, status));
        history.setEditor(admin);

        consultationHistoryRepository.save(history);

        Report report = new Report();
        report.setDescription(request.getDescription());
        report.setConsultation(consultation);

        report = reportRepository.save(report);
        consultation.setReport(report);

        return consultationRepository.save(consultation);
    }

    public boolean checkConflictSchedule(CheckConflictScheduleRequest request) {
        LocalDateTime startTimeRequest = Instant.ofEpochMilli(request.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        LocalDateTime endTimeRequest = startTimeRequest.plusMinutes(request.getDuration());

        List<Consultation> consultations = consultationRepository.findAllByConsultantIdOrderByCreatedAtDesc(request.getConsultantId());

        return consultations.stream().anyMatch(consultation -> {
            if (consultation.getConsultant() == null || consultation.getTime() == null || consultation.getDuration() == null) {
                return false;
            }
            if (!Objects.equals(consultation.getConsultant().getId(), request.getConsultantId())) {
                return false;
            }

            LocalDateTime startTime = consultation.getTime();
            LocalDateTime endTime = consultation.getTime().plusMinutes(consultation.getDuration());

            return isOverlapping(
                startTimeRequest,
                endTimeRequest,
                startTime,
                endTime
            );
        });
    }

    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
