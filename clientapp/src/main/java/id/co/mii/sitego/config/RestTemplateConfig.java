package id.co.mii.sitego.config;


import id.co.mii.sitego.util.BasicAuthInterceptorUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@AllArgsConstructor
public class RestTemplateConfig {
    private final BasicAuthInterceptorUtil authInterceptorUtil;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {
            {
                List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
                interceptors.add(authInterceptorUtil);
                setInterceptors(interceptors);
                setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            }
        };
    }
}
