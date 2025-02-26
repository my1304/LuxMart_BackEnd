package luxmart_backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Schema(description = "User entity")
@Entity(name = "DomainUser")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Schema(description = "User's username or nickname for logging in", example = "Sancos")
    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", unique = true, nullable = true)
    private String username;


    @Schema(description = "User's first name", example = "Sasha")
    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Schema(description = "User's last name", example = "Ivanyo")
    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Schema(description = "User's email address", example = "sasha@example.com")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Schema(description = "User's raw password for logging in", example = "Qwerty!123") //Password1!
    @NotBlank(message = "Password is mandatory")
    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;

    @Schema(description = "Path to user's avatar", example = "/avatar/user1.png")
    @Column(name = "avatar_path", nullable = true)
    private String avatarPath;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Schema(description = "User's username for logging in", example = "sasha.ivanov")
    public String getUsername() {
        return username;
    }

    @Schema(
            description = "True if user's account is not expired",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(
            description = "True if user's account is not locked",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(
            description = "True if user's credentials is not expired",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(description = "True if user is enabled",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username) &&
                Objects.equals(avatarPath, user.avatarPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                '}';
    }
}
