package com.nneomablessyn.packagefinder.security;

import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1145488804034890513L;

    private final PFUser user;

    public UserDetailsImpl(PFUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isLocked();
    }

    @Override
    public boolean isEnabled() {
        return !user.isLocked();
    }

    public PFUser getUser() {
        return user;
    }
}
