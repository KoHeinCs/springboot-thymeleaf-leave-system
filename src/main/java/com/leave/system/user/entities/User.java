package com.leave.system.user.entities;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean active;
    @Column(unique = true)
    private String email;
    private Integer annualLeaveNo;
    private Integer medicalLeaveNo;
    private Integer unpaidLeaveNo;

    private Integer updatedAnnualLeaveNo;
    private Integer updatedMedicalLeaveNo;
    private Integer updatedUnpaidLeaveNo;


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LeaveDetail> leaveDetailList= new ArrayList<>();

    public User(){}

    /**
     *
     * @param username
     * @param password
     * @param active
     * @param email
     * @param annualLeaveNo
     * @param medicalLeaveNo
     * @param unpaidLeaveNo
     */
    public User(String username,String password,boolean active,String email,Integer updatedAnnualLeaveNo,Integer updatedMedicalLeaveNo,Integer updatedUnpaidLeaveNo){
        super();
        this.username = username;
        this.password = password;
        this.active = active;
        this.updatedAnnualLeaveNo = updatedAnnualLeaveNo;
        this.updatedMedicalLeaveNo = updatedMedicalLeaveNo;
        this.updatedUnpaidLeaveNo = updatedUnpaidLeaveNo;
        this.email = email;
    }

    public void addUser(LeaveDetail leaveDetail){
        leaveDetailList.add(leaveDetail);
        leaveDetail.setUsers(this);
    }

    public void removeUser(LeaveDetail leaveDetail){
        leaveDetailList.remove(leaveDetail);
        leaveDetail.setUsers(null);
    }



}
