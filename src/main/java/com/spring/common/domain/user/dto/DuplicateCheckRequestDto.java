package com.spring.common.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DuplicateCheckRequestDto {

    String duplicateField;
    String content;
}
