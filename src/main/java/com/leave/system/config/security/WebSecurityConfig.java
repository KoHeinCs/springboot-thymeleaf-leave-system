package com.leave.system.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    private final CustomizedAuthenticationProvider customizedAuthenticationProvider;
    private final CustomizedAuthorizationSuccessHandler customizedAuthorizationSuccessHandler;

    public static final String PUBLIC_ACCESS[]={"/","/login","/login-error","/webjars/**","/css/**","/images/**"};

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customizedAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PUBLIC_ACCESS).permitAll()
                .antMatchers("/user").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
             .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                //.defaultSuccessUrl("/admin")
                .successHandler(customizedAuthorizationSuccessHandler)

             .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
    }
}
