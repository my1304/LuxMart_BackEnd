package luxmart_backend.security.sec_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import luxmart_backend.domain.User;
import luxmart_backend.dto.AuthorityDto;
import luxmart_backend.dto.LoginRequestDto;
import luxmart_backend.dto.UserProfileDto;
import luxmart_backend.dto.UserRegistrationDTO;
import luxmart_backend.security.sec_dto.TokenResponseDto;
import luxmart_backend.security.sec_service.AuthService;
import luxmart_backend.services.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Authorization controller", description = "Controller for security operations, login/logout, getting new tokens etc")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://luxmart.netlify.app")  // Разрешаем фронтенд
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, UserService userService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Operation(
            summary = "Login",
            description = "Logging into the system"
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object of a user logging in") LoginRequestDto loginRequest,
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponseDto(null, null)); // Возвращаем пустой объект TokenResponseDto
            }

            if (!user.isEnabled()) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto(null, null)); // Возвращаем пустой объект TokenResponseDto
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto(null, null)); // Возвращаем пустой объект TokenResponseDto
            }

            TokenResponseDto tokenDto = authService.login(loginRequest);
            if (tokenDto == null) {
                removeCookie(response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponseDto(null, null)); // Возвращаем пустой объект TokenResponseDto
            }

            Cookie cookieAccess = new Cookie("Access-Token", tokenDto.getAccessToken());
            cookieAccess.setPath("/");
            cookieAccess.setHttpOnly(true);
            cookieAccess.setSecure(true); // Должно быть true, если HTTPS
            cookieAccess.setAttribute("SameSite", "None"); // Разрешает передачу куков между доменами
            response.addCookie(cookieAccess);

            Cookie cookieRefresh = new Cookie("Refresh-Token", tokenDto.getRefreshToken());
            cookieRefresh.setPath("/");
            cookieRefresh.setHttpOnly(true);
            cookieRefresh.setSecure(true);
            cookieRefresh.setAttribute("SameSite", "None");
            response.addCookie(cookieRefresh);

            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            removeCookie(response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponseDto(null, null)); // Возвращаем пустой объект TokenResponseDto
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = (String) authentication.getPrincipal();
            User user = userService.findByEmail(userEmail);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setId(user.getId());
            userProfileDto.setEmail(user.getEmail());
            userProfileDto.setFirstName(user.getFirstName());
            userProfileDto.setLastName(user.getLastName());
            userProfileDto.setUsername(user.getUsername());
            userProfileDto.setPassword(user.getPassword());
            userProfileDto.setAvatarPath(user.getAvatarPath());

            return ResponseEntity.ok(userProfileDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Get new access token",
            description = "Receiving a new access token through providing an existing refresh token"
    )
    @GetMapping("/refresh")
    public ResponseEntity<Object> getNewAccessToken(
            @CookieValue("Refresh-Token") String refreshToken,
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        try {

            TokenResponseDto tokenDto = authService.getAccessToken(refreshToken);
            Cookie cookie = new Cookie("Access-Token", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            Cookie cookieRefresh = new Cookie("Refresh-Token", tokenDto.getRefreshToken());
            cookieRefresh.setPath("/");
            cookieRefresh.setHttpOnly(true);
            response.addCookie(cookieRefresh);
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Logout",
            description = "Logging out from the system"
    )
    @GetMapping("/logout")
    public void logout(
            @Parameter(description = "Object of a response that will be transferred to a client") HttpServletResponse response
    ) {
        removeCookie(response);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
