package com.web6.server.dto;

import java.util.Map;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Map<String, String> errors;
    private T data;

    public ApiResponse() {

    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.errors = null;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, Map<String, String> errors, T data) {
        this.success = success;
        this.message = message;
        this.errors = errors;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
