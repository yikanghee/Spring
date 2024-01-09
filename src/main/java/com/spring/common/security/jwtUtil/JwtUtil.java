package com.spring.common.security.jwtUtil;

import com.spring.common.config.RoleEnum;
import com.spring.common.exception.SecurityException;
import com.spring.common.exception.result.SecurityExceptionResult;
import com.spring.common.security.SecurityExceptionDto;
import com.spring.common.security.redis.RedisDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String REFRESH_HEADER = "Refresh";
    public static final String AUTHORIZATION_KEY = "auth";

    public static final String REFRESH_KEY = "refresh";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 60 * 100L;

    private final RedisDao redisDao;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] decode = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decode);
    }

    public void reissueAtk(String username, RoleEnum role) {
        String redisValues = redisDao.getValues(username);
        if (!this.validateToken(redisValues)) {
            throw new SecurityException(SecurityExceptionResult.SECURITY_FORBIDDEN_ERROR);
        }
        String accessToken = createAccessToken(username, role);
        if (accessToken == null) {
            throw new SecurityException(SecurityExceptionResult.SECURITY_SERVER_ERROR);
        }
    }

    public JwtTokenResponse handleJwtToken(HttpServletResponse response, String username, RoleEnum role) {
        String accessToken = this.createAccessToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        String refreshToken = this.createRefreshToken(username, role);
        response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

        redisDao.setValues(username, refreshToken, Duration.ofMillis(JwtUtil.REFRESH_TOKEN_TIME));

        return new JwtTokenResponse(accessToken, refreshToken);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String header = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(7);
        }
        return null;
    }

    public String createAccessToken(String username, RoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken(String username, RoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String createAdminToken(String username, RoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public boolean isAdminToken(String token) {
        Claims claims;
        if (token != null) {
            if (validateToken(token)) {
                claims = getUserInfoFromToken(token);
                return ((claims.get("auth").toString()).equals("ADMIN"));
            } else {
                throw new SecurityException(SecurityExceptionResult.SECURITY_SERVER_ERROR);
            }
        }
        throw new SecurityException(SecurityExceptionResult.SECURITY_SERVER_ERROR);
    }

    public JwtInfoResponse jwtInfoResponse(HttpServletRequest request) {
        String accessToken = resolveAccessToken(request);
        Claims userInfoFromToken = getUserInfoFromToken(accessToken);
        String refreshToken = resolveRefreshToken(request);
        String username = userInfoFromToken.getSubject();

        return JwtInfoResponse.builder().username(username).
                refreshToken(refreshToken).build();
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }

}
