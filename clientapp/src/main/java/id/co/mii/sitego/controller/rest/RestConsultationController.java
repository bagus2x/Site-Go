package id.co.mii.sitego.controller.rest;

import id.co.mii.sitego.model.Consultation;
import id.co.mii.sitego.model.request.CheckConflictScheduleRequest;
import id.co.mii.sitego.model.request.FinishConsultationRequest;
import id.co.mii.sitego.model.request.RejectConsultationRequest;
import id.co.mii.sitego.model.request.ScheduleConsultationRequest;
import id.co.mii.sitego.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RestConsultationController {
    private final ConsultationService consultationService;

    @PostMapping("/consultation")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Consultation request() {
        return consultationService.request();
    }

    @GetMapping("/consultations")
    public List<Consultation> getAll() {
        return consultationService.getAll();
    }

    @GetMapping("/profile/consultations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Consultation> getAllByProfile() {
        return consultationService.getAllByProfile();
    }

    @GetMapping("/consultation/{consultationId}")
    public Consultation getById(@PathVariable Integer consultationId) {
        return consultationService.getById(consultationId);
    }

    @PatchMapping("/consultation/{consultationId}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public Consultation validate(@PathVariable Integer consultationId) {
        return consultationService.validate(consultationId);
    }

    @PatchMapping("/consultation/{consultationId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public Consultation reject(@PathVariable Integer consultationId, @RequestBody RejectConsultationRequest request) {
        return consultationService.reject(consultationId, request);
    }

    @PatchMapping("/consultation/{consultationId}/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public Consultation schedule(@PathVariable Integer consultationId, @RequestBody ScheduleConsultationRequest request) {
        return consultationService.schedule(consultationId, request);
    }

    @PostMapping("/consultation/check-conflict-schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean checkConflictSchedule(@RequestBody CheckConflictScheduleRequest request) {
        return consultationService.checkConflictSchedule(request);
    }

    @PatchMapping("/consultation/{consultationId}/finish")
    @PreAuthorize("hasRole('CONSULTANT')")
    public Consultation finish(@PathVariable Integer consultationId, @RequestBody FinishConsultationRequest request) {
        return consultationService.finish(consultationId, request);
    }
}
