package com.elderlycare.controller;

import com.elderlycare.model.Elder;
import com.elderlycare.model.HealthRecord;
import com.elderlycare.service.ElderService;
import com.elderlycare.service.HealthService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ElderController.java
@Controller
@RequestMapping("/elders")
public class ElderController {

    private final ElderService elderService;
    private final HealthService healthService;

    public ElderController(ElderService elderService, HealthService healthService) {
        this.elderService = elderService;
        this.healthService = healthService;
    }

    /**
     * 分页列表（支持 AJAX 请求）
     */
    @GetMapping
    public String list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            Model model,
            HttpServletRequest request
    ) {
        // 1) 分页
        PageHelper.startPage(pageNum, 4);
        List<Elder> list = elderService.list();
        PageInfo<Elder> pageInfo = new PageInfo<>(list);

        // 2) 加入分页数据和列表
        model.addAttribute("elders", list);
        model.addAttribute("pageInfo", pageInfo);

        // 3) **新增这一行**：为新增表单提供一个空对象
        model.addAttribute("elder", new Elder());

        // 4) 区分 AJAX 片段 vs 整页
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "elders :: elderTable";
        }
        return "elders";
    }

    // 处理 表单提交，保存 Elder
    @PostMapping("/add")
    public String testAdd(Elder elder) {
        elderService.save(elder);
        // ← 重定向到 GET /elders/
        return "redirect:/elders";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Elder e = elderService.get(id);
        List<HealthRecord> recs = healthService.findByElderId(id);
        model.addAttribute("elder", e);
        model.addAttribute("healthRecords", recs);
        return "elder-detail";
    }

    /**
     * 归档老人
     **/
    @PostMapping("/{id}/archive")
    public String archiveElder(@PathVariable Long id) {
        elderService.archive(id);
        return "redirect:/elders";
    }

    /**
     * 真正删除老人（物理删）
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        elderService.delete(id);
        return "redirect:/elders";
    }
}
