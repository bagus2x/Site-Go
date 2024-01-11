package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Report;
import id.co.mii.sitego.model.request.WriteReportRequest;
import id.co.mii.sitego.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report/{reportId}")
    public Report getById(@PathVariable Integer reportId) {
        return reportService.getById(reportId);
    }

    @PutMapping("/report/{reportId}")
    public Report update(@PathVariable Integer reportId, @RequestBody WriteReportRequest request) {
        return reportService.update(reportId, request);
    }
}
