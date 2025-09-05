package com.pransquare.nems.utils;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement your logic to load user details from your data source (e.g., database)
        // Here is an example with hardcoded user details
        if ("Pransquare".equals(username)) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password("Pransquare@123") // {noop} to indicate no password encoding
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
