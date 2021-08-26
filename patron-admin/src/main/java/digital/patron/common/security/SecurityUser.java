package digital.patron.common.security;

import digital.patron.AdminMembers.domain.Admin;
import digital.patron.common.domain.Status;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    public static UserDetails fromUser(Admin admin) {
        return new org.springframework.security.core.userdetails.User(
                admin.getEmail(), admin.getPassword(),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getRole().getAuthorities()
        );
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
