package uz.atm.dto.justice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JusticeRequestDto {

    public String jsonrpc;

    public String id;

    public String method;

    public Params params;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Params {

        @JsonProperty("base_pin")
        public String basePin;

        @JsonProperty("to_check")
        public List<String> toCheck;
    }


    public JusticeRequestDto(Params params) {
        this.params = params;
    }
}
