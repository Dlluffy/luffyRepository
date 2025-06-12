package com.elderlycare.controller;

import com.elderlycare.mapper.AppointmentMapper;
import com.elderlycare.mapper.BillingMapper;
import com.elderlycare.mapper.StaffMapper;
import com.elderlycare.mapper.VisitAppointmentMapper;
import com.elderlycare.model.*;
import com.elderlycare.service.CarePlanService;
import com.elderlycare.service.CareTaskService;
import com.elderlycare.service.HealthService;
import com.elderlycare.service.RelativeElderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class family2Controller {

    @Autowired
    RelativeElderService userElderRelationService;

    @Autowired
    CarePlanService  carePlanService;

    @Autowired
    CareTaskService careTaskService;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    HealthService healthService;

    @Autowired
    BillingMapper billingMapper;

    @Autowired
    VisitAppointmentMapper  visitAppointmentMapper;

    @Autowired
    AppointmentMapper appointmentMapper;



    @GetMapping("/to-familyCareTask")
    public String careTask(Model model, HttpSession session) {
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return "redirect:/family-login";
        }

        RelativeElder relativeElder = userElderRelationService.getRelativeElderByUserId(user.getUid());
        CarePlan carePlan = carePlanService.getPlanByElderId(relativeElder.getElderId());
        model.addAttribute("carePlan", carePlan);

        // ✅ 解析 task 字段为 Map
        Map<String, String> taskMap = Map.of(); // 默认空 Map
        try {
            ObjectMapper mapper = new ObjectMapper();
            taskMap = mapper.readValue(carePlan.getTask(), Map.class);
        } catch (Exception e) {
            e.printStackTrace(); // 或记录日志
        }
        model.addAttribute("taskMap", taskMap); // 传给页面

        List<CareTaskRecord> careTaskRecords = careTaskService.listByPlan(carePlan.getPlanId());
        model.addAttribute("careTaskRecords", careTaskRecords);

        return "family-careTask";
    }

    @GetMapping("/family/health")
    public String health(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return "redirect:/family-login";
        }

        RelativeElder relativeElder = userElderRelationService.getRelativeElderByUserId(user.getUid());
        Long elderId = relativeElder.getElderId();

        CarePlan carePlan = carePlanService.getPlanByElderId(elderId);
        model.addAttribute("carePlan", carePlan);

        int offset = page * size;
        List<HealthRecord> healthRecords = healthService.findByElderIdPaged(elderId, size, offset);
        int totalRecords = healthService.countByElderId(elderId);
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("healthRecords", healthRecords);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        return "family-health";
    }



    @GetMapping("/to-familyBill")
    public String listBills(Model model,HttpSession session) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return "redirect:/family-login"; // 或跳转你自己的登录页面
        }
        RelativeElder  relativeElder= userElderRelationService.getRelativeElderByUserId(user.getUid());
        List<Billing> billingList = billingMapper.selectByElderId(relativeElder.getElderId());
        List<Billing> unpaidBills = billingMapper.selectUnpaidBillsByElderId(relativeElder.getElderId());
        model.addAttribute("unpaidBills", unpaidBills);
        model.addAttribute("billingList", billingList);
        return "family-bill";
    }

    @GetMapping("/to-familyVisit")
    public String visit(Model model,HttpSession session) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return "redirect:/family-login"; // 或跳转你自己的登录页面
        }
        RelativeElder  relativeElder= userElderRelationService.getRelativeElderByUserId(user.getUid());
        List<Appointment> appointments= appointmentMapper.selectByElderId(relativeElder.getElderId());
        model.addAttribute("appointments",appointments);
        model.addAttribute("appointmentStatus","启用");
        return "family-visit";
    }

    @PostMapping("/family/visit/submit")
    @ResponseBody
    public ResponseEntity<?> submitVisitAppointment(
            @RequestBody Appointment appointment,
            HttpSession session
    ) {
        try {
            // 1. 从 session 获取当前用户
            User user = (User) session.getAttribute("User");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录");
            }

            // 2. 查询关联的老人ID
            RelativeElder relativeElder = userElderRelationService.getRelativeElderByUserId(user.getUid());
            if (relativeElder == null) {
                return ResponseEntity.badRequest().body("未找到关联的老人信息");
            }

            // 3. 设置 appointment 的额外字段
            appointment.setUserId(Long.valueOf(user.getUid()));
            appointment.setElderId(relativeElder.getElderId());
            appointment.setStatus("待审核");
            appointment.setCreatedAt(new Date());
            appointment.setUpdatedAt(new Date());

            // 4. 插入数据库
            appointmentMapper.insert(appointment);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "服务器错误：" + e.getMessage()));
        }
    }




}
