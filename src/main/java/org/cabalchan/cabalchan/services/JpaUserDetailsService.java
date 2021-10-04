package org.cabalchan.cabalchan.services;

import java.util.function.Supplier;

import org.cabalchan.cabalchan.entities.User;
import org.cabalchan.cabalchan.repositories.UserRepository;
import org.cabalchan.cabalchan.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Supplier<UsernameNotFoundException> s =() -> new UsernameNotFoundException("Problem during authentication!");

        User u = userRepository
        .findUserByUsername(username)
        .orElseThrow(s);
        
        return new CustomUserDetails(u);
    }
}
