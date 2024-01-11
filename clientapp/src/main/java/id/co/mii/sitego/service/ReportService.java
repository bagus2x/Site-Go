package id.co.mii.sitego.service;

import id.co.mii.sitego.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final RestTemplate restTemplate;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public Report getById(Integer reportId) {
        return restTemplate.getForObject(baseUrl + "/report/" + reportId, Report.class);
    }
}
