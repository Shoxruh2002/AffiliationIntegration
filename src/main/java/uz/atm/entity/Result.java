package uz.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "results")
public class Result extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5190598237215532904L;

    @Column("request_id")
    private String requestId;

    @Column("base_pinfl")
    private String basePinfl;

    @Column("check_pinfl")
    private String checkPinfl;

    @Column("result")
    private Boolean result;

}
