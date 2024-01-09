package com.spring.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.common.security.jwtUtil.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authentication Filter
 * Once Per Requeset Forwading
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveAccessToken(request);

        if (token != null) {
            if (!jwtUtil.validateToken(token)) {
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            if (jwtUtil.isAdminToken(token)) {
                Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(token);
                // adminAuthentication
                return;
            }

            Claims userInfoFromToken = jwtUtil.getUserInfoFromToken(token);
            String username = userInfoFromToken.getSubject();

            setAuthentication(username);
        }

        filterChain.doFilter(request, response);
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void setAuthentication(String username) {
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = this.createAuthentication(username);
        emptyContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(emptyContext);
    }


    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        try {
            String json = new ObjectMapper().writeValueAsString(
                    new SecurityExceptionDto(statusCode, msg));

            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
