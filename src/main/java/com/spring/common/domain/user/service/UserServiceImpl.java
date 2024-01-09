package com.spring.common.domain.user.service;

import com.spring.common.config.RoleEnum;
import com.spring.common.domain.user.dto.*;
import com.spring.common.domain.user.entity.Profile;
import com.spring.common.exception.AccountException;
import com.spring.common.exception.result.AccountExceptionResult;
import com.spring.common.security.jwtUtil.JwtTokenResponse;
import com.spring.common.security.jwtUtil.JwtUtil;
import com.spring.common.domain.user.entity.Users;
import com.spring.common.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.resolveRefreshToken(request);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        Users users = this.findByUsername(username);

        jwtUtil.reissueAtk(username, users.getRole());
    }

    @Override
    @Transactional
    public void signup(SignupRequsetDto request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());

        if (request.getImg_url() != null) {
            Profile profile = Profile.builder().nickName(request.getNickname()).img_url(
                    request.getImg_url()).build();
            Users user = Users.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(RoleEnum.USER)
                    .profile(profile)
                    .build();

            userRepository.save(user);
        }

        Profile profile = new Profile(request.getNickname());
        Users users = Users.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(RoleEnum.USER)
                .profile(profile)
                .build();

        userRepository.save(users);
    }

    @Override
    @Transactional
    public void login(HttpServletResponse response, LoginRequestDto request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Users users = this.findByUsername(username);
        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_PASSWORD_NOT_MATCH);
        }

        JwtTokenResponse jwtTokenResponse = jwtUtil.handleJwtToken(response, users.getUsername(), users.getRole());
    }

    @Override
    public void logout(String refreshToken, String username) {

    }

    @Override
    @Transactional(readOnly = true)
    public DuplicateCheckResponseDto duplicateCheck(DuplicateCheckRequestDto request) {
        switch (request.getDuplicateField()) {
            case("username") -> {
                return new DuplicateCheckResponseDto(userRepository.existsByUsername(request.getContent()));
            }
            case("email") -> {
                return new DuplicateCheckResponseDto(userRepository.existsByEmail(request.getContent()));
            }
            case("nickname") -> {
                return new DuplicateCheckResponseDto(userRepository.existsByNickname(request.getContent()));
            }
        }
        throw new AccountException(AccountExceptionResult.ACCOUNT_WRONG_VALUE);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public void updateUser(String username, PasswordUpdateRequest request) {
        Users users = this.findByUsername(username);
        if (!users.checkAuthorization(users)) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_UPDATE_NOT_AUTHORIZATION);
        }
        String password = passwordEncoder.encode(request.getPassword());
        users.updatePassword(password);
        this.userRepository.save(users);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public void deleteUser(Long userId, String username) {
        Users users = this.findByUsername(username);

        if (!users.checkAuthorization(users)) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_DELETE_NOT_AUTHORIZATION);
        }
        this.userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public ProfileResponseDto updateProfile(String username, ProfileRequestDto request) {
        Profile profile = this.findProfileByUsername(username);

        profile.update(request);
        return new ProfileResponseDto(profile);
    }

    @Override
    @Transactional
    public ProfileResponseDto showProfile(Long userId) {
        Profile profile = userRepository.findById(userId)
                .orElseThrow(() -> new AccountException(AccountExceptionResult.ACCOUNT_NOT_FOUND))
                .getProfile();

        return new ProfileResponseDto(profile);
    }

    private Profile findProfileByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new AccountException(AccountExceptionResult.ACCOUNT_NOT_FOUND)
        ).getProfile();
    }

    private Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new AccountException(AccountExceptionResult.ACCOUNT_NOT_FOUND)
        );
    }


}
