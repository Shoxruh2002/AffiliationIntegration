package uz.atm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import uz.atm.properties.JusticeProperties;


/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Configuration
public class BaseConfig {
    private final JusticeProperties justiceProperties;

    public BaseConfig(JusticeProperties justiceProperties) {
        this.justiceProperties = justiceProperties;
    }

    @Bean(name = "justice-web-client")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(justiceProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .filter(logFilter())
                .build();
    }
}
