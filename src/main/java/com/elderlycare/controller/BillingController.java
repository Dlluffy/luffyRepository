package com.elderlycare.controller;

import com.elderlycare.model.Billing;
import com.elderlycare.service.BillingService;
import com.elderlycare.service.ElderService;
import com.elderlycare.pay.BillingPayService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final BillingPayService billingPayService;
    private final BillingService billingService;
    private final ElderService elderService;

    public BillingController(BillingPayService billingPayService, BillingService billingService, ElderService elderService) {
        this.billingPayService = billingPayService;
        this.billingService = billingService;
        this.elderService = elderService;
    }

    /**
     * 全部账单分页列表，支持 AJAX 片段刷新
     */
    @GetMapping
    public String list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            Model model,
            HttpServletRequest request
    ) {
        PageInfo<Billing> pageInfo = billingService.listAll(pageNum, 5);
        model.addAttribute("billings", pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);

        // 如果是 AJAX 请求，就只返回表格片段
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "billing :: billingTable";
        }

        // 普通请求，连同新增表单的数据一起渲染
        model.addAttribute("elders", elderService.list());
        model.addAttribute("newBilling", new Billing());
        return "billing";
    }

    /**
     * 处理“新增账单”表单提交
     */
    @PostMapping
    public String create(@ModelAttribute("newBilling") Billing newBilling) {
        // 默认 status 可以在 Service 里设，或者这里直接：
        newBilling.setStatus("未支付");
        billingService.save(newBilling);
        return "redirect:/billing";
    }

    /**
     * 查看单条账单详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Billing bill = billingService.get(id);
        model.addAttribute("billing", bill);
        return "billing-detail";
    }

    /**
     * 标记账单已支付
     */
//    @PostMapping("/{id}/pay")
//    public String markPaid(@PathVariable("id") Long id) {
//        Billing bill = billingService.get(id);
//        bill.setStatus("已支付");
//        bill.setPaidAt(new Date());
//        billingService.update(bill);
//        return "redirect:/billing";
//    }

    @PostMapping("/{id}/pay")
    public void pay(@PathVariable Long id,
                    HttpServletResponse resp) throws Exception {
        String html = billingPayService.buildPayForm(id);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);        // 浏览器渲染后立刻跳转支付宝
    }

    /**
     * 删除账单
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        billingService.delete(id);
        return "redirect:/billing";
    }

    /**
     * （可选）查看某位老人所有账单
     */
    @GetMapping("/elder/{elderId}")
    public String listByElder(@PathVariable Long elderId, Model model) {
        List<Billing> list = billingService.listByElderId(elderId);
        model.addAttribute("billings", list);
        return "billing";
    }

}
