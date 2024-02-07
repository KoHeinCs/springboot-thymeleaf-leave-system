package com.leave.system.config.security;

import com.leave.system.user.enums.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomizedAuthorizationSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(CustomizedAuthorizationSuccessHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final String url = getUrl(authentication,request);
        if (response.isCommitted()){
            return;
        }
        redirectStrategy.sendRedirect(request,response,url);
    }
    private String getUrl(Authentication authentication,HttpServletRequest request){
        String url = null;
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        logger.info(authorities.toString());
        List<String> roles = new ArrayList<>();
        authorities.forEach(auth ->{
           roles.add(auth.getAuthority());
        });
        if (roles.contains(RoleEnum.ROLE_ADMIN.toString())){
            logger.info("admin");
            /*
                    On login success , we can set different value of Max Inactive Interval for different roles/users in seconds
             */
            request.getSession(false).setMaxInactiveInterval(30*3600);
            url = "/admin";
        }else if (roles.contains(RoleEnum.ROLE_USER.toString())){
            logger.info("/user");
            request.getSession(false).setMaxInactiveInterval(60*3600);
            url = "/user";
        }else {
            logger.info("error");
            throw new IllegalStateException();
        }
        return url;
    }
}
