package com.spring.common.dto;

import lombok.Getter;

@Getter
public class StatusResponse {

    private int statusCode;
    private String message;

    public static StatusResponse valueOf(ResponseMessages response) {
        return new StatusResponse(response.getStatusCode(), response.getMessage());
    }

    public StatusResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
