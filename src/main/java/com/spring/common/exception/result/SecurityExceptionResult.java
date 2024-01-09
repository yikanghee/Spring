package com.spring.common.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SecurityExceptionResult {

    SECURITY_FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    SECURITY_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 정보가 없습니다."),
    SECURITY_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 서버 에러"),
    ;

    private final HttpStatus status;
    private final String message;
}
