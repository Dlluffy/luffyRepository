package com.elderlycare.controller;

import com.elderlycare.model.Appointment;
import com.elderlycare.service.AppointmentService;
import com.elderlycare.service.ElderService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService apptSvc;
    private final ElderService elderSvc;

    public AppointmentController(AppointmentService apptSvc, ElderService elderSvc) {
        this.apptSvc = apptSvc;
        this.elderSvc = elderSvc;
    }

    @GetMapping
    public String list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                       Model model,
                       HttpServletRequest request) {
        PageInfo<Appointment> pageInfo = apptSvc.listAll(pageNum, 5);
        model.addAttribute("appointments", pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "appointments :: apptTable";
        }

        Appointment newAppt = new Appointment();
        newAppt.setUserId(1L);

        model.addAttribute("elders", elderSvc.list());
        model.addAttribute("newAppt", newAppt);
        return "appointments";
    }

    @PostMapping
    public String create(@ModelAttribute("newAppt") Appointment appt,
                         @RequestParam(name = "fee", required = false) BigDecimal fee) {

        apptSvc.save(appt);                      // 保存预约并写回 ID

        if (fee != null && fee.compareTo(BigDecimal.ZERO) > 0) {
            apptSvc.createBilling(appt.getAppointmentId(), fee);
        }

        return "redirect:/appointments";
    }


    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        apptSvc.cancel(id);
        return "redirect:/appointments";
    }

    @PostMapping("/{id}/pay")
    public String pay(@PathVariable Long id) {
        apptSvc.pay(id);
        return "redirect:/appointments";
    }
}
