package com.spring.common.config;

import com.spring.common.exception.AccountException;
import com.spring.common.exception.result.AccountExceptionResult;
import com.spring.common.domain.user.entity.Profile;
import com.spring.common.domain.user.entity.Users;
import com.spring.common.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#username", cacheManager = "cacheManager")
    public Users cacheUser(String username) {
        Users users = userRepository.findByUsername(username).orElseThrow(
                () -> new AccountException(AccountExceptionResult.ACCOUNT_NOT_FOUND)
        );

        return Users.builder()
                .username(users.getUsername())
                .id(users.getId())
                .role(users.getRole())
                .email(users.getEmail())
                .profile(Profile.builder().img_url(users.getProfile().getImg_url()).nickName(users.getProfile().getNickName()).build())
                .password(users.getPassword())
                .build();
    }
}
