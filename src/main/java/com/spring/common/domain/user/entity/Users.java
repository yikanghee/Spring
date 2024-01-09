package com.spring.common.domain.user.entity;

import com.spring.common.config.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(length = 25, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @Enumerated
    private RoleEnum role;

    @Embedded
    private Profile profile;

    public Users(String username, String password, Profile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    /**
     * 서비스 메소드
     */
    public boolean checkAuthorization(Users users) {
        return Objects.equals(this.id, users.getId());
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
