package com.spring.common.exception;

import com.spring.common.exception.result.AccountExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AccountException extends RuntimeException {

    private final AccountExceptionResult exceptionResult;
}
