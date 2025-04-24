package com.nuhi.Nuhi.security;

import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return userRepository.findByEmail(authentication.getName());
        }
        return Optional.empty();
    }
}

//    public static Optional<User> getCurrentUser(){
//
//        //fetch details of current logged in users
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication !=null && authentication.getPrincipal() instanceof User){
//            return Optional.of((User) authentication.getPrincipal()); //return logged user object
//        }
//        return Optional.empty();
//    }


