package uz.atm.dto.justice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JusticeResponse {

    public String jsonrpc;

    public String id;

    public Map<String, Boolean> result;

    public JusticeError error;

    public JusticeResponse(String jsonrpc, String id, Map<String, Boolean> result) {
        this.jsonrpc = jsonrpc;
        this.id = id;
        this.result = result;
    }

    public JusticeResponse(String jsonrpc, String id, JusticeError error) {
        this.jsonrpc = jsonrpc;
        this.id = id;
        this.error = error;
    }
}
