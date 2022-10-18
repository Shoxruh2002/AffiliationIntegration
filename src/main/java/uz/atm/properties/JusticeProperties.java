package uz.atm.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Getter
@ConfigurationProperties(prefix = "justice.api")
public class JusticeProperties {

    private String baseUrl;

    private Api api;

    @Data
    public static class Api {
        private String checkApi;
    }

}
