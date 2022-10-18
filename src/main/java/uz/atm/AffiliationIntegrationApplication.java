package uz.atm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.atm.properties.JusticeProperties;



@SpringBootApplication
@OpenAPIDefinition
@EnableConfigurationProperties(value = {
        JusticeProperties.class
})
public class AffiliationIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffiliationIntegrationApplication.class, args);
    }

}
