package com.example.aquafin.services;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.aquafin.models.User;

public class CustomUserDetails implements UserDetails{

    private final User user;

    public CustomUserDetails(User user2) {
        this.user = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(( )->user.getRole());
    }

    public String getFullname() {
		return user.getFullname();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}



}
