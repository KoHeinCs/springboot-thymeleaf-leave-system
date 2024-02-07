package com.leave.system.user.service;
import com.leave.system.user.dto.UserDTO;
import com.leave.system.user.dto.UserLeaveDTO;
import com.leave.system.user.entities.LeaveDetail;
import com.leave.system.user.entities.User;

import java.util.*;

public interface UserService {


    public void initializeUsersAndRoles();
    public List<User> findAll();
    public Optional<User> findByEmail(String email);
    public void saveUser(User user);
    public boolean createOrUpdateUser(UserDTO userDTO);
    /**
     * calculate number of days between two dates excluding weekend
     * @param startDate
     * @param endDate
     * @return number of days between two dates excluding weekend
     */
    public int calNorOfDaysExcWeekEnd(Date startDate,Date endDate);
    public User prepareUserByReducingRequestedNoOfDays(int requestedNoOfDays, User user, String leave_type);
    public List<String> leaveTypes();
    public int getCurrentAvailableLeaveBaseOnLeaveType(LeaveDetail leaveDetail, User user) ;
    public List<UserLeaveDTO> getLeaveDetailDTOList(User user);
    public UserDTO findUserDTOById(Long id);


}
