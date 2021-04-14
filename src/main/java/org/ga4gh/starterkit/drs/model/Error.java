package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Error extends Exception {

    public static final long serialVersionUID = 1L;

    private String msg;

    private int statusCode;

    public Error(String message, int statusCode) {
        this.msg = message;
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public String getMessage() {
        return "status_code: " + statusCode + " message: " + msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
