package uz.atm.dto.justice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@NoArgsConstructor
@AllArgsConstructor
public class JusticeResponse {

    public String jsonrpc;

    public String id;

    public Map<String, Boolean> result;
}
