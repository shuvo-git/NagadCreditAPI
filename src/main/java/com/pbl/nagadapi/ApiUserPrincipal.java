package com.pbl.nagadapi;

import com.pbl.nagadapi.entity.ApiUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ApiUserPrincipal implements UserDetails {
    private ApiUsers apiUser;

    public ApiUserPrincipal(ApiUsers apiUser){
        this.apiUser = apiUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.apiUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.apiUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
