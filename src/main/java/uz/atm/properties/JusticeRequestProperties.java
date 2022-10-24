package uz.atm.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static uz.atm.utils.BaseUtils.generateRandomUUID;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/24/22 10:02 AM
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "justice.request")
public class JusticeRequestProperties {

    private String jsonRpc;

    private String id = generateRandomUUID();

    private String method;
}
