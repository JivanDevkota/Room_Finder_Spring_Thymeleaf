package com.nsu.roomfinder.service;

import com.nsu.roomfinder.entity.User;
import com.nsu.roomfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException(username));
        user.getRoles().forEach(role -> System.out.println("User Role: " + role.getName()));
        return new MyUser(user);
    }

}
