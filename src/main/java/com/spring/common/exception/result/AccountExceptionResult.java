package com.spring.common.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountExceptionResult {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    ACCOUNT_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "입력한 정보가 틀립니다."),
    ACCOUNT_WRONG_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    ACCOUNT_UPDATE_NOT_AUTHORIZATION(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다."),
    ACCOUNT_DELETE_NOT_AUTHORIZATION(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
