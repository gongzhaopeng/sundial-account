package cn.benbenedu.sundial.account.model;

import lombok.Data;

@Data
public class ErrorResponse {

    private ErrorResponseCode code;
    private String detail;

    public enum ErrorResponseCode {
        Undefined
        // TODO
    }
}
