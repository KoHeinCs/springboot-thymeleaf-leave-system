package com.leave.system.config.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomizedAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(CustomizedAuthenticationProvider.class);
    private final CustomUserServiceImpl customUserServiceImpl;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = null;
        Authentication auth = null;
        String emailFromLogin = authentication.getName();
        String passwordFromLogin = authentication.getCredentials().toString();
        user = (User) customUserServiceImpl.loadUserByUsername(emailFromLogin);
        boolean isSamePassword = passwordEncoder.matches(passwordFromLogin,user.getPassword());
        if (
                passwordFromLogin == null ||
                !isSamePassword ||
                ! emailFromLogin.equals(user.getUsername())
        ){
            logger.info("worng user / password ");
            throw new UsernameNotFoundException("wrong user/password ");
        }
        if (
                emailFromLogin.equals(user.getUsername()) &&
                passwordEncoder.matches(passwordFromLogin,user.getPassword())
        ){
            logger.info("correct user / password");
            auth = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
        }

        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
