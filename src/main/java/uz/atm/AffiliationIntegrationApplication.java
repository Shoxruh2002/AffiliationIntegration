package uz.atm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import reactor.core.publisher.Hooks;
import uz.atm.properties.JusticeAPiProperties;
import uz.atm.properties.JusticeRequestProperties;
import uz.atm.properties.RabbitMQProperties;


@SpringBootApplication
@OpenAPIDefinition
@EnableConfigurationProperties(value = {
        JusticeAPiProperties.class,
        JusticeRequestProperties.class,
        RabbitMQProperties.class
})
public class AffiliationIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffiliationIntegrationApplication.class, args);
        Hooks.onOperatorDebug();
    }

}
