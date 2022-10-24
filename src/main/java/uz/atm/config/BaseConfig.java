package uz.atm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import uz.atm.properties.JusticeAPiProperties;


/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Configuration
public class BaseConfig {
    private final JusticeAPiProperties justiceAPiProperties;

    public BaseConfig(JusticeAPiProperties justiceAPiProperties) {
        this.justiceAPiProperties = justiceAPiProperties;
    }

    @Bean(name = "justice-web-client")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(justiceAPiProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .filter(logFilter())
                .build();
    }
}
