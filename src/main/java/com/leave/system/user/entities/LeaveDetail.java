package com.leave.system.user.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "leave_detail")
@Data
public class LeaveDetail {
    @Id
    @Column(name = "leave_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Start date cannot be empty")
    @NotEmpty(message = "Start date cannot be empty")
    private String start_date;
    @NotNull(message = "End date cannot be empty")
    @NotEmpty(message = "End date cannot be empty")
    private String end_date;
    @NotEmpty(message = "Leave type cannot be empty")
    @NotEmpty(message = "Leave type cannot be empty")
    private String leave_type;
    @NotNull(message = "Mobile number cannot be empty")
    @NotEmpty(message = "Mobile number cannot be empty")
    private String mobile;
    @Size(max = 255,message = "Exceed reason length")
    @NotEmpty(message = "Reason cannot be empty")
    private String reason;
    private String status;
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User users;

//    @Override
//    public boolean equals(Object o) {
//       if (this == o ) return true ;
//       if(!(o instanceof LeaveDetail)) return false;
//       return id != null && id.equals(((LeaveDetail)o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
}
