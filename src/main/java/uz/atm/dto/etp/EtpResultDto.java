package uz.atm.dto.etp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:52 AM
 **/
@AllArgsConstructor
@NoArgsConstructor
public class EtpResultDto {

    public Long requestId;

    public String base;

    public String check;

    public Boolean result;
}
