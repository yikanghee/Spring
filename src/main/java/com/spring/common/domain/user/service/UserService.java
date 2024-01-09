package com.spring.common.domain.user.service;

import com.spring.common.domain.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void reissue(HttpServletRequest request, HttpServletResponse response);
    void signup(SignupRequsetDto requsetDto);

    void login(HttpServletResponse response, LoginRequestDto request);

    void logout(String refreshToken, String username);

    DuplicateCheckResponseDto duplicateCheck(DuplicateCheckRequestDto request);

    void updateUser(String username, PasswordUpdateRequest request);

    void deleteUser(Long userId, String username);

    ProfileResponseDto updateProfile(String username, ProfileRequestDto request);

    ProfileResponseDto showProfile(Long userId);


}
