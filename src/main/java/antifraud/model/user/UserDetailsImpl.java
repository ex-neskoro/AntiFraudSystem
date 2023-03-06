package antifraud.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final User user;
    private final List<UserRole> rolesAndAuthorities;
    boolean isAccountNonLocked;

    public UserDetailsImpl(User user) {
        username = user.getUsername();
        password = user.getPassword();
        rolesAndAuthorities = new ArrayList<>();
        rolesAndAuthorities.add(user.getRole());
        this.isAccountNonLocked = user.isAccountNonLocked();
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    public User getUserEntity() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
