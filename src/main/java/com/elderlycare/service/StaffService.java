package com.elderlycare.service;

import com.elderlycare.model.Staff;
import java.util.List;

public interface StaffService {
    /** 列出所有护理员 **/
    List<Staff> listAllNurses();
}
