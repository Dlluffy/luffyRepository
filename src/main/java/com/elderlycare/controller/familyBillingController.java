package com.elderlycare.controller;

import com.elderlycare.mapper.BillingMapper;
import com.elderlycare.model.Billing;
import com.elderlycare.model.User;
import com.elderlycare.service.RelativeElderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/billing")
public class familyBillingController {

    @Autowired
    private BillingMapper billService;

    @Autowired
    RelativeElderService userElderRelationService;

    // 点击“立即支付”跳转到支付确认页
    @GetMapping("/pay/confirm")
    public String payConfirm(@RequestParam Long billId, Model model, RedirectAttributes redirectAttributes) {
        Billing bill = billService.selectByPrimaryKey(billId);
        if (bill == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "账单不存在");
            return "redirect:/billing";
        }
        if ("已支付".equals(bill.getStatus())) {
            redirectAttributes.addFlashAttribute("msg", "该账单已支付，无需重复支付");
            return "redirect:/billing";
        }
        model.addAttribute("bill", bill);
        return "family-payConfirm";  // 支付确认页面
    }

    // 确认支付，修改状态
    @PostMapping("/pay")
    public String doPay(@RequestParam Long billId, RedirectAttributes redirectAttributes) {
        Billing bill = billService.selectByPrimaryKey(billId);
        if (bill == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "账单不存在");
            return "redirect:/to-familyBill";
        }
        if ("已支付".equals(bill.getStatus())) {
            redirectAttributes.addFlashAttribute("msg", "该账单已支付，无需重复支付");
            return "redirect:/to-familyBill";
        }
        bill.setStatus("已缴");
        bill.setPaidAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        billService.updateByPrimaryKey(bill);

        redirectAttributes.addFlashAttribute("msg", "支付成功");
        return "redirect:/to-familyBill";

    }

    @GetMapping("/generateMonthBill")
    public String getThisMonthBills(Model model, HttpSession session) {
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return "redirect:/login"; // 或跳转你自己的登录页面
        }
        Long elderId = userElderRelationService.getRelativeElderByUserId(user.getUid()).getElderId();
        List<Billing> bills = billService.selectByElderId(elderId);

        // 提取当前年月字符串，例如 "2025-05"
        String currentYearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 过滤出本月的账单
        List<Billing> currentMonthBills = bills.stream()
                .filter(bill -> currentYearMonth.equals(bill.getBillingMonth()))
                .collect(Collectors.toList());

        // 计算本月账单总金额，初始值为0
        BigDecimal totalAmount = currentMonthBills.stream()
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算本月欠费总金额（状态为“欠费”的账单）
        BigDecimal unpaidAmount = currentMonthBills.stream()
                .filter(bill -> "欠费".equals(bill.getStatus()))
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //获取欠费的账单

        // 放入模型
        model.addAttribute("currentMonthBills", currentMonthBills);
        model.addAttribute("billingList", bills);
        model.addAttribute("message", "以下为本月账单：");
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("unpaidAmount", unpaidAmount);
        List<Billing> unpaidBills = billService.selectUnpaidBillsByElderId(elderId);
        model.addAttribute("unpaidBills", unpaidBills);
        return "family-bill";
    }



}
