package com.spring.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponseEntity {

    public static final ResponseEntity<StatusResponse> RESPONSE_OK =
            ResponseEntity.status(HttpStatus.OK).body(StatusResponse.valueOf(ResponseMessages.SUCCESS));
    public static final ResponseEntity<StatusResponse> CREATED_OK =
            ResponseEntity.status(HttpStatus.CREATED).body(StatusResponse.valueOf(ResponseMessages.CREATED_SUCCESS));
    public static final ResponseEntity<StatusResponse> DELETE_OK =
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(StatusResponse.valueOf(ResponseMessages.DELETE_SUCCESS));
}
