package com.tripbros.server.security;

public enum SecurityExceptionMessage {
    UNAUTHORIZED("인증되지 않았습니다."),

    ;

    private final String message;

    SecurityExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
