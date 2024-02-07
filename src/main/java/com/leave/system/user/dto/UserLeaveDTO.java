package com.leave.system.user.dto;

import lombok.Data;

/**
 * @author HeinHtetAung
 * @created 14/Sep/2020 -11:45 PM
 **/
@Data
public class UserLeaveDTO {
    private String leaveType;
    private Integer leaveEntitlement;
    private Integer taken;
    private Integer balance;

    public UserLeaveDTO(String leaveType, Integer leaveEntitlement, Integer taken, Integer balance){
        this.leaveType = leaveType;
        this.leaveEntitlement = leaveEntitlement;
        this.taken = taken;
        this.balance = balance;
    }

}
