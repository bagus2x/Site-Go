package id.co.mii.sitego.service;

import id.co.mii.sitego.model.Report;
import id.co.mii.sitego.model.request.WriteReportRequest;
import id.co.mii.sitego.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final Validator validator;

    public Report getById(Integer reportId) {
        return reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report is not found"));
    }

    public Report update(Integer reportId, WriteReportRequest request) {
        Set<ConstraintViolation<WriteReportRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report is not found"));
        report.setDescription(request.getDescription());
        report.setUpdatedAt(LocalDateTime.now());

        return reportRepository.save(report);
    }
}
