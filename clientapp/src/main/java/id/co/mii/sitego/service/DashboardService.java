package id.co.mii.sitego.service;

import id.co.mii.sitego.model.response.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final RestTemplate restTemplate;

    @Value("${server.baseUrl}")
    private String baseUrl;

    public DashboardResponse getDashboardStat() {
        return restTemplate.getForObject(baseUrl + "/dashboard", DashboardResponse.class);
    }
}
