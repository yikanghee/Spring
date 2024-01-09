package com.spring.common.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequsetDto {

    @Size(min = 4, max = 12, message = "최소4자, 최대12자")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "소문자와 숫자를 포함하여 4자 이상 12자 이하로 적어주세요.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", message = "소문자와 대문자 특수문자 그리고 숫자를 포함하여 8자 이상 15자 이하로 적어주세요.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @NotEmpty(message = "본인 인증 가능한 이메일을 입력해주세요.")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "소문자와 숫자를 포함하여 4자 이상 12자 이하로  적어주세요.")
    private String nickname;

    private String img_url;
}
