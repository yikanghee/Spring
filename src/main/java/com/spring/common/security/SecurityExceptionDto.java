package com.spring.common.security;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecurityExceptionDto {

    private int statusCode;
    private String message;

    public SecurityExceptionDto(int statusCod, String message) {
        this.statusCode = statusCod;
        this.message = message;
    }

}
