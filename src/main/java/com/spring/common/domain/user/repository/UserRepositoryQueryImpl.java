package com.spring.common.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.common.exception.AccountException;
import com.spring.common.exception.result.AccountExceptionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.spring.common.domain.user.entity.QUsers.users;

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

    @Override
    public void existsByUserInfo(String username, String email, String nickname) {

        boolean nickNameRes = jpaQueryFactory
                .from(users)
                .where(users.profile.nickName.eq(nickname))
                .select(users.profile.nickName)
                .setHint("org.hibernate.readOnly", true)
                .fetchFirst() != null;

        if (nickNameRes) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_DUPLICATION_NICKNAME);
        }

        boolean emailRes = jpaQueryFactory
                .from(users)
                .where(users.email.eq(email))
                .select(users)
                .fetchFirst() != null;
        if (emailRes) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_DUPLICATION_EMAIL);
        }

        boolean userNameRes = jpaQueryFactory
                .from(users)
                .where(users.username.eq(username))
                .select(users)
                .fetchFirst() != null;

        if (userNameRes) {
            throw new AccountException(AccountExceptionResult.ACCOUNT_DUPLICATION_USERNAME);
        }
    }


}
