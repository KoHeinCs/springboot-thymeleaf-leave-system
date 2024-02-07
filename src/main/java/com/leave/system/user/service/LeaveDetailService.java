package com.leave.system.user.service;
import com.leave.system.user.entities.LeaveDetail;
import java.util.List;

/**
 * @author HeinHtetAung
 * @created 09/Sep/2020 -9:49 PM
 **/
public interface LeaveDetailService {
    public void saveLeaveDetail(LeaveDetail leaveDetail);
    public List<LeaveDetail> findAllLeaveTransactions();
    public LeaveDetail findById(Long id);
}
