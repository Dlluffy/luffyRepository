package com.elderlycare.controller;

import com.elderlycare.model.*;
import com.elderlycare.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/care-plans")
public class CarePlanController {
    private final CarePlanService planService;
    private final CareTaskService taskService;
    private final ElderService elderService;
    private final HealthService healthService;
    private final StaffService staffService;

    public CarePlanController(CarePlanService ps,
                              CareTaskService ts,
                              ElderService es, HealthService hs, StaffService ss) {
        this.planService = ps;
        this.taskService = ts;
        this.elderService = es;
        this.healthService = hs;
        this.staffService = ss;
    }

    /**
     * 护理计划列表 + 新建表单
     */
    @GetMapping
    public String list(
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            Model m) {

        // 1) 开启分页：第 pageNum 页，每页 10 条
        PageHelper.startPage(pageNum, 10);
        List<CarePlan> plans = planService.listAll();
        PageInfo<CarePlan> pageInfo = new PageInfo<>(plans);

        // 收集所有不重复的 elderId
        Set<Long> elderIds = plans.stream()
                .map(CarePlan::getElderId)
                .collect(Collectors.toSet());
        // 一次性查出所有老人并映射 elderId -> name
        Map<Long, String> elderNames = elderService.findByIds(elderIds)
                .stream()
                .collect(Collectors.toMap(Elder::getElderId, Elder::getName));

        List<Elder> elders = elderService.list();
        m.addAttribute("elders", elders);
        m.addAttribute("plans", plans);
        m.addAttribute("pageInfo", pageInfo);
        m.addAttribute("elderNames", elderNames);
        m.addAttribute("plan", new CarePlan());
        return "care-plans";
    }

    @PostMapping
    public String save(CarePlan cp) {
        planService.save(cp);
        return "redirect:/care-plans";
    }

    /**
     * 查看并执行某计划的任务
     */
    @GetMapping("/{id}/tasks")
    public String tasks(@PathVariable Long id, Model m) {
        CarePlan cp = planService.getById(id);
        List<CareTaskRecord> recs = taskService.listByPlan(id);
        Elder elder = elderService.get(cp.getElderId());

        // 新增：查询所有护理员
        List<Staff> nurses = staffService.listAllNurses();
        m.addAttribute("nurses", nurses);

        m.addAttribute("plan", cp);
        m.addAttribute("elder", elder);
        m.addAttribute("records", recs);
        m.addAttribute("newRec", new CareTaskRecord());
        return "care-tasks";
    }

    @PostMapping("/{id}/archive")
    public String archivePlan(@PathVariable Long id) {
        planService.archive(id);
        return "redirect:/care-plans";
    }

    @PostMapping("/{id}/delete")
    public String deletePlan(@PathVariable Long id) {
        planService.delete(id);
        return "redirect:/care-plans";
    }

    @PostMapping("/{id}/tasks")
    public String markDone(@PathVariable Long id,
                           CareTaskRecord rec,
                           @RequestParam(required = false) String bloodPressure,
                           @RequestParam(required = false) BigDecimal bloodSugar,
                           @RequestParam(required = false) String medication,
                           @RequestParam(required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                           LocalDate recordDate) {

        // 1) 保存护理任务记录
        rec.setPlanId(id);
        taskService.markDone(rec);

        // 2) 如果填写了健康档案的任意字段，就保存 HealthRecord
        boolean hasBp = StringUtils.hasText(bloodPressure);
        boolean hasSugar = (bloodSugar != null);
        boolean hasMed = StringUtils.hasText(medication);
        if (hasBp || hasSugar || hasMed) {
            HealthRecord hr = new HealthRecord();
            // elderId 从 rec 中获取（rec 里应该有 planId→可以拿到 plan，再拿 elderId）
            Long elderId = planService.getById(id).getElderId();
            hr.setElderId(elderId);
            hr.setBloodPressure(bloodPressure);
            hr.setBloodSugar(bloodSugar);
            hr.setMedication(medication);
            LocalDate ld = (recordDate != null ? recordDate : LocalDate.now());
            Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            hr.setRecordDate(date);
            healthService.save(hr);
        }

        return "redirect:/care-plans/" + id + "/tasks";
    }
}
