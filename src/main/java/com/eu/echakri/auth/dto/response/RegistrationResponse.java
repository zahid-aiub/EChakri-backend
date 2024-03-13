package com.eu.echakri.auth.dto.response;

public class RegistrationResponse {
    private final int code;
    private final String message;

    public RegistrationResponse(int token, String message) {
        this.code = token;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
