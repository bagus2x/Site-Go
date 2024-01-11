package id.co.mii.sitego.controller.rest;

import id.co.mii.sitego.model.Report;
import id.co.mii.sitego.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RestReportController {
    private final ReportService reportService;

    @GetMapping("/report/{reportId}")
    public Report getById(@PathVariable Integer reportId) {
        return reportService.getById(reportId);
    }
}
