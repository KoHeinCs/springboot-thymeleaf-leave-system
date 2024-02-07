package com.leave.system.user.service.impl;

import com.leave.system.user.entities.LeaveDetail;
import com.leave.system.user.repository.LeaveDetailRepository;
import com.leave.system.user.service.LeaveDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HeinHtetAung
 * @created 22/Sep/2020 -9:38 PM
 **/
@Service
@RequiredArgsConstructor
public class LeaveDetailServiceImpl implements LeaveDetailService {
    private final LeaveDetailRepository leaveDetailRepository;
    @Override
    public void saveLeaveDetail(LeaveDetail leaveDetail) {
        leaveDetailRepository.save(leaveDetail);
    }

    @Override
    public List<LeaveDetail> findAllLeaveTransactions() {
        return leaveDetailRepository.findAll();
    }

    @Override
    public LeaveDetail findById(Long id) {
        return leaveDetailRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not Found "+id));
    }
}
