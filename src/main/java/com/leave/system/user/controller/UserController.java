package com.leave.system.user.controller;
import com.leave.system.user.dto.UserLeaveDTO;
import com.leave.system.user.entities.LeaveDetail;
import com.leave.system.user.entities.User;
import com.leave.system.user.enums.RoleEnum;
import com.leave.system.user.service.LeaveDetailService;
import com.leave.system.user.service.UserService;
import com.leave.system.user.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.leave.system.user.constants.TemplateConstant.*;


@Controller
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final LeaveDetailService leaveDetailService;

    @GetMapping(value = {"",LEAVE_TRANSACTION})
    public String goLeaveTransaction(Authentication authentication,Model model){
        List<LeaveDetail> leaveDetailList = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        boolean role_admin=authorities.contains(new SimpleGrantedAuthority(RoleEnum.ROLE_ADMIN.toString()));
        if(role_admin){
            /** admin can view all user leave transactions**/
            leaveDetailList = leaveDetailService.findAllLeaveTransactions();
        }else {
            /** user can only view his leave transactions**/
            User user=userService.findByEmail(authentication.getName()).orElseThrow(()->new EntityNotFoundException(authentication.getName()+" not found"));
            leaveDetailList  = user.getLeaveDetailList();
        }

        model.addAttribute("leaveDetailList",leaveDetailList);
        return USER_LEAVE_TRANSACTION;
    }

    @GetMapping(APPLY_LEAVE)
    public String user(Model model){
        logger.info("Go To Apply Leave Page");
        model.addAttribute("leaveDetail",new LeaveDetail());
        model.addAttribute("leaveTypes",userService.leaveTypes());
        return USER_APPLY_LEAVE;
    }

    @PostMapping(APPLY_LEAVE)
    public String applyLeave(@ModelAttribute @Valid LeaveDetail leaveDetail, BindingResult bindingResult, Model model, Authentication authentication){

        logger.info(leaveDetail.toString());
        if (bindingResult.hasErrors()){
                    return goBackApplyLeavePage(model,false);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = formatter.parse(leaveDetail.getStart_date());
            endDate = formatter.parse(leaveDetail.getEnd_date());
        }catch (ParseException e){
            logger.error("cannot parse string date ");
        }
        int compareNo = startDate.compareTo(endDate);

        if (compareNo == 1){ // start date is grater than end date so not allowed
            return goBackApplyLeavePage(model,true);
        }
        else {
            int requestedNoOfDays = 0;
            String userEmail = authentication.getName();
            User  user =userService.findByEmail(userEmail).orElse(new User());
            leaveDetail.setStatus(StatusEnum.PENDING.name());
            leaveDetail.setUserName(user.getUsername());
            leaveDetail.setUsers(user);
            int currentAvailableLeaveNo = userService.getCurrentAvailableLeaveBaseOnLeaveType(leaveDetail,user);
            if (compareNo == 0){// two dates are equal
                requestedNoOfDays = 1;
                if (requestedNoOfDays<=currentAvailableLeaveNo){
                        user = userService.prepareUserByReducingRequestedNoOfDays(requestedNoOfDays,user,leaveDetail.getLeave_type());
                }else {
                    // show exceeded requested days error message
                }
            }else{ // start date is smaller than end date
                requestedNoOfDays = userService.calNorOfDaysExcWeekEnd(startDate,endDate);
                if (requestedNoOfDays<=currentAvailableLeaveNo){
                    user = userService.prepareUserByReducingRequestedNoOfDays(requestedNoOfDays,user,leaveDetail.getLeave_type());
                }else {
                    // show exceeded requested days error message
                }
            }
            leaveDetailService.saveLeaveDetail(leaveDetail);
            userService.saveUser(user);
        }
        return "redirect:"+USER_LEAVE_TRANSACTION;
    }


    @GetMapping(LEAVE_DETAIL)
    public String leaveDetail(Authentication authentication,Model model)
    {
        String email = authentication.getName();
        User user=userService.findByEmail(email).orElse(new User());
        List<UserLeaveDTO> userLeaveDTOList = userService.getLeaveDetailDTOList(user);
        model.addAttribute("userLeaveDTOList", userLeaveDTOList);
        return USER_LEAVE_DETAIL;
    }

    private String goBackApplyLeavePage(Model model, Boolean hasCompareDateError){
        model.addAttribute("duplicated",hasCompareDateError);
        model.addAttribute("leaveTypes",userService.leaveTypes());
        return USER_APPLY_LEAVE;
    }






}
