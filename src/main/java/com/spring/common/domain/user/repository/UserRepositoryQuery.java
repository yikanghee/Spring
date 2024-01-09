package com.spring.common.domain.user.repository;

public interface UserRepositoryQuery {
    boolean existsByNickname(String nickname);
}
