package com.nuhi.Nuhi.service;

import com.nuhi.Nuhi.exception.UserNotFoundException;
import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService  implements UserDetailsService {

private final UserRepository userRepository;



//public CustomUserDetailService(UserRepository userRepository){
//    this.userRepository=userRepository;
//}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()))
        );
    }



    }

