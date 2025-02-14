package luxmart_backend.security.sec_service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import luxmart_backend.domain.User;
import luxmart_backend.security.sec_dto.AuthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
@Service
public class TokenService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public TokenService(
            @Value("${access.key}") String accessKey,
            @Value("${refresh.key}") String refreshKey
    ) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
    }

    public String generateAccessToken(@Nonnull User user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expirationInstant = currentDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(accessKey)
                .claim("name", user.getUsername())
                .claim("email",user.getEmail())
                .compact();
    }

    public String generateRefreshToken(@Nonnull User user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expirationInstant = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    public boolean validateAccessToken(@Nonnull String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(@Nonnull String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(@Nonnull String token, @Nonnull SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAccessClaims(@Nonnull String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRefreshClaims(@Nonnull String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    private Claims getClaims(@Nonnull String token, @Nonnull SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

// Так как роли не используются, можете удалить код, связанный с обработкой ролей
    public AuthInfo generateAuthInfo(Claims claims) {
        String username = (String) claims.get("email");
        return new AuthInfo(username);
    }
/*    public AuthInfo generateAuthInfo(Claims claims) {
        String username = (String) claims.get("email");
        List<LinkedHashMap<String, String>> rolesList = (List<LinkedHashMap<String, String>>) claims.get("roles");

        if (rolesList != null) {
            for (LinkedHashMap<String, String> roleEntry : rolesList) {
                String roleName = roleEntry.get("authority");
            }
        } else {
            return new AuthInfo(username);
        }

        return new AuthInfo(username);
    }*/
}