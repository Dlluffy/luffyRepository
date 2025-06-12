package com.elderlycare.service.impl;

import com.elderlycare.mapper.StaffMapper;
import com.elderlycare.model.Staff;
import com.elderlycare.service.StaffService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffMapper staffMapper;
    public StaffServiceImpl(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Override
    public List<Staff> listAllNurses() {
        return staffMapper.selectAllNurses();
    }
}
