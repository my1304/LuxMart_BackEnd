package luxmart_backend.security.sec_filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import luxmart_backend.security.sec_dto.AuthInfo;
import luxmart_backend.security.sec_service.TokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

    private TokenService service;

    public TokenFilter(TokenService service) {
        this.service = service;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) request);

        if (token != null && service.validateAccessToken(token)) {
            Claims claims = service.getAccessClaims(token);
            AuthInfo authInfo = service.generateAuthInfo(claims);
            authInfo.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }

        chain.doFilter(request, response);
    }

    // Метод доработан.
    // Теперь считываем токен не только из заголовка запроса, но и из куки.
    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Access-Token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Извлекаем токен из заголовка Authorization
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Возвращаем сам токен без "Bearer "
        }
        return null;
    }
}