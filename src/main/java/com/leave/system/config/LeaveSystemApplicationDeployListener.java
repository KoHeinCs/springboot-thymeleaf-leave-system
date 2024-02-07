package com.leave.system.config;

import com.leave.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveSystemApplicationDeployListener implements ApplicationListener<ApplicationReadyEvent> {
    Logger logger= LoggerFactory.getLogger(LeaveSystemApplicationDeployListener.class);
    private final UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("****************** loading every  application start ************** ");
        userService.initializeUsersAndRoles();
    }
}
