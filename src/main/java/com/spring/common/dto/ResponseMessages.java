package com.spring.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessages {
    SUCCESS(200, "요청 성공"),
    CREATED_SUCCESS(201, "생성 성공"),
    DELETE_SUCCESS(204, "삭제 성공"),
    ;
    private final int statusCode;
    private final String message;
}
