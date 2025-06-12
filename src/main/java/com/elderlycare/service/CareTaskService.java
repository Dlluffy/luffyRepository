// src/main/java/com/elderlycare/service/CareTaskService.java
package com.elderlycare.service;

import com.elderlycare.model.CareTaskRecord;
import java.util.List;

public interface CareTaskService {
    List<CareTaskRecord> listByPlan(Long planId);
    void markDone(CareTaskRecord rec);
}
