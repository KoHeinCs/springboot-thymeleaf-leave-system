package com.leave.system.user.service.impl;

import com.leave.system.user.dto.UserDTO;
import com.leave.system.user.dto.UserLeaveDTO;
import com.leave.system.user.enums.LeaveEnum;
import com.leave.system.user.entities.LeaveDetail;
import com.leave.system.user.entities.Role;
import com.leave.system.user.entities.User;
import com.leave.system.user.enums.RoleEnum;
import com.leave.system.user.repository.UserRepository;
import com.leave.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author HeinHtetAung
 * @created 22/Sep/2020 -9:54 PM
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${leave.system.admin.name}")
    private String adminName;
    @Value("${leave.system.admin.password}")
    private String adminPassword;

    @Value("${leave.system.user.name}")
    private String userName;
    @Value("${leave.system.user.password}")
    private String userPassword;

    @Override
    public void initializeUsersAndRoles() {
        if(userRepository.count() ==0){
            Role roleAdmin = new Role(RoleEnum.ROLE_ADMIN.toString());
            Role roleUser =  new Role(RoleEnum.ROLE_USER.toString());

            /*  For admin   */
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleAdmin);
            roleSet.add(roleUser);

            User admin=new User(adminName,passwordEncoder.encode(adminPassword),true,"admin@gmail.com",7,10,99);
            admin.setAnnualLeaveNo(7);
            admin.setMedicalLeaveNo(10);
            admin.setUnpaidLeaveNo(99);
            admin.setRoles(roleSet);

            /*  For user     */
            Set<Role> userRoleSet = new HashSet<>();
            userRoleSet.add(roleUser);

            User user = new User(userName,passwordEncoder.encode(userPassword),true,"user@gmail.com",7,10,99);
            user.setAnnualLeaveNo(7);
            user.setMedicalLeaveNo(10);
            user.setUnpaidLeaveNo(99);
            user.setRoles(userRoleSet);


            /*  save users  */
            userRepository.saveAll(Arrays.asList(admin,user));
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean createOrUpdateUser(UserDTO userDTO) {
        Set<Role> roleSet = new HashSet<>();
        if (userDTO.getRoles().contains("ROLE_ADMIN")){
            Role roleAdmin = new Role(RoleEnum.ROLE_ADMIN.toString());
            roleSet.add(roleAdmin);
        }
        if (userDTO.getRoles().contains("ROLE_USER")){
            Role roleUser =  new Role(RoleEnum.ROLE_USER.toString());
            roleSet.add(roleUser);
        }
       User user = userRepository.findById(userDTO.getId()).orElse(new User());
       user.setRoles(roleSet);
       user.setActive(true);

        if (user.getId() == null){
            /** create **/
                prepareCreteOrUpdateUser(user,userDTO);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setUpdatedAnnualLeaveNo(userDTO.getAnnualLeaveNo());
                user.setUpdatedMedicalLeaveNo(userDTO.getMedicalLeaveNo());
                user.setUpdatedUnpaidLeaveNo(userDTO.getUnpaidLeaveNo());


        }else {
            /** update **/
            prepareCreteOrUpdateUser(user,userDTO);
        }
        user.setEmail(userDTO.getEmail());
        try{
            saveUser(user);
        }catch (DataIntegrityViolationException exception){
            return false;
        }

        return true;



    }
    private void prepareCreteOrUpdateUser(User user,UserDTO userDTO){
        user.setUsername(userDTO.getUsername());
        user.setAnnualLeaveNo(userDTO.getAnnualLeaveNo());
        user.setMedicalLeaveNo(userDTO.getMedicalLeaveNo());
        user.setUnpaidLeaveNo(userDTO.getUnpaidLeaveNo());
    }

    @Override
    public int calNorOfDaysExcWeekEnd(Date startDate, Date endDate) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(startDate);
        calendar2.setTime(endDate);
        int numberOfDays = 1 ;
        while (calendar1.before(calendar2)){
            if (
                    (Calendar.SATURDAY != calendar1.get(Calendar.DAY_OF_WEEK)) &&
                            (Calendar.SUNDAY != calendar1.get(Calendar.DAY_OF_WEEK))
            ){
                numberOfDays++;
            }
            calendar1.add(Calendar.DATE,1);
        }
        return numberOfDays;
    }

    @Override
    public User prepareUserByReducingRequestedNoOfDays(int requestedNoOfDays, User user, String leave_type) {
        if (leave_type.equalsIgnoreCase(LeaveEnum.ANNUAL.getDescription())){
            user.setUpdatedAnnualLeaveNo(user.getUpdatedAnnualLeaveNo() - requestedNoOfDays );
        }else if (leave_type.equalsIgnoreCase(LeaveEnum.MEDICAL.getDescription())){
            user.setUpdatedMedicalLeaveNo(user.getUpdatedMedicalLeaveNo()-requestedNoOfDays);
        }else if (leave_type.equalsIgnoreCase(LeaveEnum.UNPAID.getDescription())){
            user.setUpdatedUnpaidLeaveNo(user.getUpdatedUnpaidLeaveNo() - requestedNoOfDays);
        }
        return user;
    }

    @Override
    public List<String> leaveTypes() {
        List<String> leaveTypes = Arrays.asList(
                LeaveEnum.ANNUAL.getDescription(),
                LeaveEnum.MEDICAL.getDescription(),
                LeaveEnum.UNPAID.getDescription()
        );
        return leaveTypes;
    }

    @Override
    public int getCurrentAvailableLeaveBaseOnLeaveType(LeaveDetail leaveDetail, User user) {
        int currentAvailableLeave = 0;
        if (leaveDetail.getLeave_type().equalsIgnoreCase(LeaveEnum.ANNUAL.getDescription())){
            currentAvailableLeave = user.getUpdatedAnnualLeaveNo();
        }else if (leaveDetail.getLeave_type().equalsIgnoreCase(LeaveEnum.MEDICAL.getDescription())){
            currentAvailableLeave = user.getUpdatedMedicalLeaveNo();
        }else if (leaveDetail.getLeave_type().equalsIgnoreCase(LeaveEnum.UNPAID.getDescription())){
            currentAvailableLeave = user.getUpdatedUnpaidLeaveNo();
        }
        return currentAvailableLeave;
    }

    @Override
    public List<UserLeaveDTO> getLeaveDetailDTOList(User user) {
        List<UserLeaveDTO> userLeaveDTOList = new ArrayList<>();
        UserLeaveDTO leaveDetailForAnnual =new UserLeaveDTO(LeaveEnum.ANNUAL.name(),user.getAnnualLeaveNo(),
                user.getAnnualLeaveNo()-user.getUpdatedAnnualLeaveNo(),user.getUpdatedAnnualLeaveNo());
        userLeaveDTOList.add(leaveDetailForAnnual);
        UserLeaveDTO leaveDetailForUnpaid =new UserLeaveDTO(LeaveEnum.UNPAID.name(),
                user.getUnpaidLeaveNo(),user.getUnpaidLeaveNo()-user.getUpdatedUnpaidLeaveNo(),user.getUpdatedUnpaidLeaveNo());
        userLeaveDTOList.add(leaveDetailForUnpaid);
        UserLeaveDTO leaveDetailForMedical =new UserLeaveDTO(LeaveEnum.MEDICAL.name(),
                user.getMedicalLeaveNo(),user.getMedicalLeaveNo()-user.getUpdatedMedicalLeaveNo(),user.getUpdatedMedicalLeaveNo());
        userLeaveDTOList.add(leaveDetailForMedical);
        return userLeaveDTOList;
    }

    @Override
    public UserDTO findUserDTOById(Long id) {
       User user =userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("user not found"));
        UserDTO userDTO=new UserDTO(user);
        return  userDTO;
    }



}
