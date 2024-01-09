package com.spring.common.exception;

import com.spring.common.exception.result.SecurityExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SecurityException extends RuntimeException {

    private final SecurityExceptionResult exceptionResult;
}
