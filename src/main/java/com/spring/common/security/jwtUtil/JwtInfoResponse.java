package com.spring.common.security.jwtUtil;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtInfoResponse {
    private String username;
    private String refreshToken;
}
