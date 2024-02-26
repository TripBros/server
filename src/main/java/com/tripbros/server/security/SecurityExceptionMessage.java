package com.tripbros.server.security;

public enum SecurityExceptionMessage {
    UNAUTHORIZED("인증되지 않았습니다."),
    MalformedJwt("잘못된 서명의 JWT입니다."),
    ExpiredJwt("유효기간이 만료된 JWT입니다."),
    UnsupportedJwt("지원하지 않는 형식의 JWT입니다.")

    ;

    private final String message;

    SecurityExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
