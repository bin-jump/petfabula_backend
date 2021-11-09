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
//        res.setMessage(code.getMessage());
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
        VALIDATION_FAILED(1001, "VALIDATION_FAILED"),
        AUTHENTICATION_FAILED(1002, "AUTHENTICATION_FAILED"),
        INVALID_OPERATION(1003, "VALIDATION_OPERATION"),
        NOT_FOUND(1004, "NOT_FOUND"),
        NO_OPERATION_OBJECT(1005, "NO_OPERATION_OBJECT"),
        AUTHORIZATION_FAILED(1006, "AUTHORIZATION_FAILED"),
        AUTHORIZATION_LIMIT_REACHED(1007, "AUTHORIZATION_LIMIT_REACHED"),

        LOGIN_REQUIRED(2001, "LOGIN_REQUIRED"),
        NO_PERMISSION(2002, "NO_PERMISSION"),

        INTERNAL_ERROR(3001, "INTERNAL_ERROR");

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
