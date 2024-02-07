package com.leave.system.user.controller;

import com.leave.system.user.dto.UserDTO;
import com.leave.system.user.entities.LeaveDetail;
import com.leave.system.user.entities.User;
import com.leave.system.user.service.LeaveDetailService;
import com.leave.system.user.service.UserService;
import com.leave.system.user.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static com.leave.system.user.constants.TemplateConstant.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(ADMIN)
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;
    private final LeaveDetailService leaveDetailService;

    @GetMapping(value = {"","/"})
    public String adminUserPage(Model model){
        prepareUserList(model);
        return ADMIN_USER;
    }
    private void prepareUserList(Model model){
        model.addAttribute("userList",userService.findAll());
        model.addAttribute("userDTO",new UserDTO());
    }

    @PostMapping
    @ResponseBody
    public String createUser(@RequestBody  UserDTO userDTO, Model model){
        System.out.println("create user");
        System.out.println(userDTO.toString());
        boolean isSuccess = userService.createOrUpdateUser(userDTO);
        if (isSuccess)
            return "success";
        else
            return "failed";

    }

    @GetMapping(APPROVE+"/{id}")
    public String approveApplyLeave(@PathVariable("id") Long id){
        logger.info("id: "+id);
        LeaveDetail leaveDetail =leaveDetailService.findById(id);
        leaveDetail.setStatus(StatusEnum.APPROVED.name());
        leaveDetailService.saveLeaveDetail(leaveDetail);
        return "redirect:"+USER_LEAVE_TRANSACTION;
    }



    @RequestMapping(value = "/user/{userId}",method =RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId){
        UserDTO userDTO=userService.findUserDTOById(userId);
        return ResponseEntity.ok(userDTO);
    }



}
