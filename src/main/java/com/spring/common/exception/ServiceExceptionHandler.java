package com.spring.common.exception;

import com.spring.common.exception.result.AccountExceptionResult;
import com.spring.common.exception.result.SecurityExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountException.class})
    public ResponseEntity<ErrorResponse> handlerAccountException(final AccountException e) {
        log.warn("AccountException occur : ", e);
        return this.makeErrorResponseEntity(e.getExceptionResult());
    }

    @ExceptionHandler({java.lang.SecurityException.class})
    public ResponseEntity<ErrorResponse> handlerSecurityException(final SecurityException e) {
        log.warn("SecurityException occur : ", e);
        return this.makeErrorResponseEntity(e.getExceptionResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final AccountExceptionResult e) {

        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.name(), e.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final SecurityExceptionResult e) {

        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.name(), e.getMessage()));
    }
}
