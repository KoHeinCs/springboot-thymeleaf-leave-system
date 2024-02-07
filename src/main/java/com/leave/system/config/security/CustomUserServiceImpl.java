package com.leave.system.config.security;

import com.leave.system.user.entities.User;
import com.leave.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(CustomUserServiceImpl.class);
   private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String emailFromLogin) throws UsernameNotFoundException {
        User  userFromDB=userService.findByEmail(emailFromLogin).orElseThrow(() -> new UsernameNotFoundException(emailFromLogin+" not found"));
        Set<String> roles=new HashSet<>();
        if (emailFromLogin.equals(userFromDB.getEmail())){
            userFromDB.getRoles().forEach(role -> {
                roles.add(role.getName());
            });
        }
        else {
            throw new UsernameNotFoundException(emailFromLogin +" not found ");
        }
        List<GrantedAuthority> authorities=new ArrayList<>();
        roles.forEach(roleName ->{
            authorities.add(new SimpleGrantedAuthority(roleName));
        });

        return new org.springframework.security.core.userdetails.User(
                userFromDB.getEmail(),userFromDB.getPassword(),true,true,true,true,authorities
        );
    }
}
