package vn.thecode.jobhunter.config;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.thecode.jobhunter.service.UserService;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

    private final UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.thecode.jobhunter.domain.User user = this.userService.handldeGetUserByUserName(username);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority));
    }

}
