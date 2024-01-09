package com.spring.common.security;

import com.spring.common.config.RoleEnum;
import com.spring.common.domain.user.entity.Users;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Spring Security에서 사용자의 정보를 담는 인터페이스
 * Spring Security에서 구현한 클래스를 사용자 정보로 인식하고 인증작업을 하는 VO
 */
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Users users;
    private Map<String, Object> attributes;

    public UserDetailsImpl(Users users) {
        this.users = users;
    }

    // OAuth2
    public UserDetailsImpl(Users users, Map<String, Object> attributes) {
        this.users = users;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        RoleEnum role = users.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    public Users getUsers() {
        return users;
    }

    public Long getUserId() {
        return users.getId();
    }

    @Override
    public String getPassword() {
        return this.users.getPassword();
    }

    @Override
    public String getUsername() {
        return this.users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
