package com.spring.common.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordUpdateRequest {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", message = "소문자와 대문자 특수문자 그리고 숫자를 포함하여 8자 이상 15자 이하로 적어주세요.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    public PasswordUpdateRequest() {

    }

    public PasswordUpdateRequest(String password) {
        this.password = password;
    }
}
