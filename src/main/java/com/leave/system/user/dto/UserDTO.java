package com.leave.system.user.dto;
import com.leave.system.user.entities.Role;
import com.leave.system.user.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author HeinHtetAung
 * @created 21/Sep/2020 -10:32 AM
 **/
@Data
public class UserDTO {


    private Long id;
    private String username;
    private String password;
    private boolean active;
    private String email;
    private Integer annualLeaveNo;
    private Integer medicalLeaveNo;
    private Integer unpaidLeaveNo;
    private  List<String> roles;
    public   UserDTO(){

    }
    public UserDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.email = user.getEmail();
        this.annualLeaveNo = user.getAnnualLeaveNo();
        this.medicalLeaveNo = user.getMedicalLeaveNo();
        this.unpaidLeaveNo = user.getUnpaidLeaveNo();
        roles = prepareRoleForDTO(user.getRoles());
    }

    private List<String> prepareRoleForDTO(Set<Role> roles){
        List<String> rolesList = new ArrayList<>();
        for (Role r:roles){
            rolesList.add(r.getName());
        }
        return rolesList;
    }
}
