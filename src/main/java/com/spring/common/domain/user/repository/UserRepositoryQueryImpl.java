package com.spring.common.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.example.haru.common.domain.user.entity.QUsers.users;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname) {
        return jpaQueryFactory
                .from(users)
                .where(users.profile.nickName.eq(nickname))
                .select(users.profile.nickName)
                .setHint("org.hibernate.readOnly", true)
                .fetchFirst() != null;
    }
}
