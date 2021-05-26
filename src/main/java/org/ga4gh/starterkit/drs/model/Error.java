package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Directly from DRS specification, error object returned to client whenever a
 * server or client-side error is encountered during a DRS controller function
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Error extends Exception {

    public static final long serialVersionUID = 1L;

    private String msg;

    private int statusCode;

    /**
     * Instantiates a new Error
     * @param message helpful error message
     * @param statusCode HTTP status code
     */
    public Error(String message, int statusCode) {
        this.msg = message;
        this.statusCode = statusCode;
    }

    /**
     * Retrieve msg
     * @return error message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Retrieve full message for logging purposes
     * @return message indicating error code and msg
     */
    public String getMessage() {
        return "status_code: " + statusCode + " message: " + msg;
    }

    /**
     * Assign msg
     * @param msg error message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Retrieve status code
     * @return HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Assign statusCode
     * @param statusCode HTTP status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
