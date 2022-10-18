package uz.atm.dto.dto;

import java.awt.print.Book;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:58 AM
 **/
public class ResultDto {
    public Boolean status;

    public String base;

    public String check;

    public Boolean result;


    public ResultDto(Boolean status) {
        this.status = status;
    }

    public ResultDto(String base, String check, Boolean result) {
        this.status = Boolean.TRUE;
        this.base = base;
        this.check = check;
        this.result = result;
    }
}
