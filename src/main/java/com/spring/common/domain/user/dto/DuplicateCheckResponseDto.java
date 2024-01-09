package com.spring.common.domain.user.dto;

import lombok.Getter;

@Getter
public class DuplicateCheckResponseDto {
    boolean duplicate;

    public DuplicateCheckResponseDto(boolean duplicate) {
        this.duplicate = duplicate;
    }
}
