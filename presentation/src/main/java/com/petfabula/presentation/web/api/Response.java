package com.petfabula.presentation.web.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {

    private int code;

    private String message;

    private T data;

    private Map<String, Object> errors = new HashMap<>();

    static public <T> Response<T> ok() {
        Response<T> res = new Response<>();
        return res;
    }

    static public <T> Response<T> ok(T data) {
        Response<T> res = Response.ok();
        res.setData(data);
        return res;
    }

    static public <T> Response<T> failed(ResponseCode code) {
        Response<T> res = Response.ok();
        res.setCode(code.getCode());
        res.setMessage(code.getMessage());
        return res;
    }

    static public <T> Response<T> failedOnValidation() {
        Response res = failed(ResponseCode.VALIDATION_FAILED);
        return res;
    }

    static public <T> Response<T> failedInternal() {
        Response res = failed(ResponseCode.INTERNAL_ERROR);
        return res;
    }

    /**
     * 1*** blame data
     * 2*** blame user
     * 3*** blame system
     */
    public enum ResponseCode{

        OK(0, "ok"),
        VALIDATION_FAILED(1001, "validation failed"),
        AUTHENTICATION_FAILED(1002, "authentication failed"),
        BAD_REQUEST(1003, "bad request"),
        NOT_FOUND(1004, "resource not found"),
        NO_OPERATION_OBJECT(1005, "operation resource not found"),
        AUTHORIZATION_FAILED(1006, "authorization failed"),


        LOGIN_REQUIRED(2001, "login required"),
        NO_PERMISSION(2002, "no permission"),

        INTERNAL_ERROR(3001, "internal error");

        private final int code;
        private final String message;

        ResponseCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }
}
