package zero.eight.donut.config.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import zero.eight.donut.dto.auth.MemberDto;
import zero.eight.donut.exception.Error;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class JwtUtils {

    @Value("${secret.time.access}")
    private long accessTokenTime; // 30분;
    @Value("${secret.time.refresh}")
    private long refreshTokenTime; // 14일;
    @Value("${secret.key}")
    private String jwtSecretKey;
    private final StringRedisTemplate stringRedisTemplate;

    public String createAccessToken(MemberDto member) {
        Claims claims = Jwts.claims();
        claims.put("name", member.getName());
        claims.put("role", member.getRole().getRole());
        long validTime = accessTokenTime;
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }


    public String createRefreshToken(MemberDto member) {
        Claims claims = Jwts.claims();
        claims.put("name", member.getName());
        long validTime = refreshTokenTime;
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();

        updateUserRefreshToken(member, refreshToken);

        return refreshToken;
    }

    public void updateUserRefreshToken(MemberDto member, String refreshToken) {
        stringRedisTemplate.opsForValue().set(member.getName(), refreshToken, refreshTokenTime, TimeUnit.MILLISECONDS);
    }

    public String getUserRefreshToken(String name) {
        return stringRedisTemplate.opsForValue().get(name);
    }

    public void deleteRefreshTokenByName(String name) {
        if (getUserRefreshToken(name) != null) {
            stringRedisTemplate.delete(name);
        }
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException(Error.JWT_TOKEN_NOT_EXISTS);
        }
        if(isLogout(token)){
            throw new UnauthorizedException(Error.LOG_OUT_JWT_TOKEN);
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(Error.INVALID_JWT_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(Error.JWT_EXPIRED);
        }
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = getClaims(token);

        if (claims.get("role") == null) {
            throw new UnauthorizedException(Error.INVALID_JWT_EXCEPTION);
        }

        // 클레임에서 권한 정보 취득
        String role = getRoleValueFromToken(token);

        // UserDetails 객체를 생성하여 Authentication 반환
        UserDetails principal = new User(getNameFromToken(token), "", Collections.singleton(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    public void setBlackList(String accessToken) {
        Long expiration = getExpiration(accessToken);
        stringRedisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isLogout(String accessToken) {
        return !ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(accessToken));
    }

    public Long getExpiration(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - new Date().getTime();
    }

    public String getNameFromToken(String token) {
        return getClaims(token).get("name").toString();
    }

    public String getRoleValueFromToken(String token) {
        return getClaims(token).get("role").toString();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }

    public String resolveJWT(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}
